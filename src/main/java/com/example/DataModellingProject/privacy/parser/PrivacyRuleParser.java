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

        // Parse OwnershipDefinitions
        NodeList ownerships = document.getElementsByTagName("Ownership");
        for (int i = 0; i < ownerships.getLength(); i++) {
            Element el = (Element) ownerships.item(i);
            config.getOwnershipDefinitions().put(
                    el.getAttribute("role"),
                    el.getAttribute("attribute")
            );
        }

        //  Parse RuleSets
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

        // NEW: Parse RowFilter (Ignoring Condition)
        NodeList rowFilterNodes = tableRuleEl.getElementsByTagName("RowFilter");
        if (rowFilterNodes.getLength() > 0) {
            Element rfEl = (Element) rowFilterNodes.item(0);
            RowFilter rowFilter = new RowFilter();

            // Only set ownership if the attribute actually exists
            if (rfEl.hasAttribute("ownership")) {
                rowFilter.setOwnership(rfEl.getAttribute("ownership"));
            }
            rule.setRowFilter(rowFilter);
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

    private AnonymizationRule parseAnonymization(Element anonEl) {
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