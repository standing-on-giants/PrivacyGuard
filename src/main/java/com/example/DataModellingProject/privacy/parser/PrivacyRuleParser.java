package com.example.DataModellingProject.privacy.parser;

import com.example.DataModellingProject.privacy.model.*;
import org.springframework.stereotype.Component;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

@Component
public class PrivacyRuleParser {

    public PrivacyConfig parse(InputStream xmlInputStream) throws Exception {
        PrivacyConfig config = new PrivacyConfig();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlInputStream);
        document.getDocumentElement().normalize();

        // Parse RuleSets (OwnershipDefinitions was removed in the new schema)
        NodeList ruleSetNodes = document.getElementsByTagName("RuleSet");
        for (int i = 0; i < ruleSetNodes.getLength(); i++) {
            Element ruleSetEl = (Element) ruleSetNodes.item(i);
            RuleSet ruleSet = new RuleSet();
            ruleSet.setRole(ruleSetEl.getAttribute("role"));

            // Parse TableRules within this RuleSet
            NodeList tableRuleNodes = ruleSetEl.getElementsByTagName("TableRule");
            for (int j = 0; j < tableRuleNodes.getLength(); j++) {
                Element tableRuleEl = (Element) tableRuleNodes.item(j);
                TableRule tableRule = parseTableRule(tableRuleEl);
                ruleSet.getTableRules().put(tableRule.getTableName(), tableRule);
            }

            config.getRuleSets().put(ruleSet.getRole(), ruleSet);
        }

        return config;
    }

    private TableRule parseTableRule(Element tableRuleEl) {
        TableRule rule = new TableRule();
        rule.setTableName(tableRuleEl.getAttribute("table"));

        // Parse RowFilter using the new AST structure
        NodeList rowFilterNodes = tableRuleEl.getElementsByTagName("RowFilter");
        if (rowFilterNodes.getLength() > 0) {
            rule.setRowFilter(parseRowFilter((Element) rowFilterNodes.item(0)));
        }

        // Parse MaskColumns
        NodeList maskNodes = tableRuleEl.getElementsByTagName("MaskColumns");
        if (maskNodes.getLength() > 0) {
            NodeList columns = ((Element) maskNodes.item(0)).getElementsByTagName("Column");
            for (int i = 0; i < columns.getLength(); i++) {
                rule.getMaskColumns().add(columns.item(i).getTextContent());
            }
        }

        // Parse AllowOnlyColumns
        NodeList allowNodes = tableRuleEl.getElementsByTagName("AllowOnlyColumns");
        if (allowNodes.getLength() > 0) {
            NodeList columns = ((Element) allowNodes.item(0)).getElementsByTagName("Column");
            for (int i = 0; i < columns.getLength(); i++) {
                rule.getAllowOnlyColumns().add(columns.item(i).getTextContent());
            }
        }

        // Parse Anonymization
        NodeList anonNodes = tableRuleEl.getElementsByTagName("Anonymization");
        if (anonNodes.getLength() > 0) {
            rule.setAnonymization(parseAnonymization((Element) anonNodes.item(0)));
        }

        return rule;
    }

    // --- NEW RECURSIVE AST PARSING METHODS ---

    private RowFilter parseRowFilter(Element rfEl) {
        RowFilter rowFilter = new RowFilter();
        NodeList children = rfEl.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) node;
                if (el.getTagName().equals("Condition")) {
                    rowFilter.setCondition(parseCondition(el));
                } else if (el.getTagName().equals("Logic")) {
                    rowFilter.setLogic(parseLogic(el));
                }
            }
        }
        return rowFilter;
    }

    private Logic parseLogic(Element logicEl) {
        Logic logic = new Logic();
        logic.setOperator(logicEl.getAttribute("operator"));

        NodeList children = logicEl.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) node;
                if (el.getTagName().equals("Condition")) {
                    logic.getConditions().add(parseCondition(el));
                } else if (el.getTagName().equals("Logic")) {
                    logic.getLogics().add(parseLogic(el)); // Recursion!
                }
            }
        }
        return logic;
    }

    private Condition parseCondition(Element condEl) {
        Condition condition = new Condition();
        condition.setOperator(condEl.getAttribute("operator"));

        Element leftEl = getSingleChildElement(condEl, "LeftOperand");
        if (leftEl != null) {
            condition.setLeftOperand(parseOperand(leftEl));
        }

        Element rightEl = getSingleChildElement(condEl, "RightOperand");
        if (rightEl != null) {
            condition.setRightOperand(parseOperand(rightEl));
        }

        return condition;
    }

    private Operand parseOperand(Element operandContainerEl) {
        Operand operand = new Operand();
        NodeList children = operandContainerEl.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) node;
                String tag = el.getTagName();
                String text = el.getTextContent().trim();

                switch (tag) {
                    case "Column": operand.setColumn(text); break;
                    case "ContextAttribute": operand.setContextAttribute(text); break;
                    case "StringValue": operand.setStringValue(text); break;
                    case "IntegerValue": operand.setIntegerValue(Integer.parseInt(text)); break;
                    case "DoubleValue": operand.setDoubleValue(Double.parseDouble(text)); break;
                    case "Null": operand.setNullValue("NULL"); break;
                    case "ListValue": operand.setListValue(parseListValue(el)); break;
                }
            }
        }
        return operand;
    }

    private ListValue parseListValue(Element listEl) {
        ListValue listValue = new ListValue();
        NodeList children = listEl.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) node;
                if (el.getTagName().equals("StringValue")) {
                    listValue.getStringValues().add(el.getTextContent().trim());
                } else if (el.getTagName().equals("IntegerValue")) {
                    listValue.getIntegerValues().add(Integer.parseInt(el.getTextContent().trim()));
                }
            }
        }
        return listValue;
    }

    /**
     * Helper to safely extract a specific child element (ignores empty text nodes)
     */
    private Element getSingleChildElement(Element parent, String tagName) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(tagName)) {
                return (Element) node;
            }
        }
        return null;
    }

    // --- KEEP YOUR EXISTING parseAnonymization METHOD HERE ---
    private AnonymizationRule parseAnonymization(Element anonEl) {
        // ... (Your existing code for parsing ARX attributes and K/L/T models) ...
        AnonymizationRule anonRule = new AnonymizationRule();

        // Attributes
        NodeList attributes = anonEl.getElementsByTagName("Attribute");
        for (int i = 0; i < attributes.getLength(); i++) {
            Element attr = (Element) attributes.item(i);
            anonRule.getAttributes().put(attr.getAttribute("column"), attr.getAttribute("type"));
        }

        // Privacy Models
        NodeList privacyModels = anonEl.getElementsByTagName("PrivacyModels");
        if (privacyModels.getLength() > 0) {
            Element modelsEl = (Element) privacyModels.item(0);

            // K-Anonymity
            NodeList kNodes = modelsEl.getElementsByTagName("KAnonymity");
            if (kNodes.getLength() > 0) {
                anonRule.setKAnonymity(Integer.parseInt(((Element) kNodes.item(0)).getAttribute("k")));
            }

            // L-Diversity
            NodeList lNodes = modelsEl.getElementsByTagName("LDiversity");
            for (int i = 0; i < lNodes.getLength(); i++) {
                Element lEl = (Element) lNodes.item(i);
                AnonymizationRule.LDiversity ld = new AnonymizationRule.LDiversity();
                ld.setColumn(lEl.getAttribute("column"));
                ld.setL(Integer.parseInt(lEl.getAttribute("l")));
                anonRule.getLDiversities().add(ld);
            }

            // T-Closeness
            NodeList tNodes = modelsEl.getElementsByTagName("TCloseness");
            for (int i = 0; i < tNodes.getLength(); i++) {
                Element tEl = (Element) tNodes.item(i);
                AnonymizationRule.TCloseness tc = new AnonymizationRule.TCloseness();
                tc.setColumn(tEl.getAttribute("column"));
                tc.setT(Double.parseDouble(tEl.getAttribute("t")));
                anonRule.getTClosenesses().add(tc);
            }
        }
        return anonRule;
    }
}