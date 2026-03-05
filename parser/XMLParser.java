import org.deidentifier.arx.*;
import org.deidentifier.arx.criteria.KAnonymity;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.*;

public class ARXPrivacyAPI {

    /**
     * Entry point to anonymize data on-the-fly based on XML schema rules.
     */
    public String anonymizeOnTheFly(String role, String tableName, List<String[]> rawData, String xmlPath) throws Exception {
        
        // Create ARX Data object from raw input
        Data data = Data.create(rawData);
        
        // Parse XML to get Column Rules
        Map<String, String> columnPolicies = parseXMLRules(xmlPath, role, tableName);
        
        // Apply ARX Data Definition (Mapping XML to ARX Types)
        DataDefinition definition = data.getDefinition();
        String[] headers = rawData.get(0); // Assuming first row is headers

        for (String colName : headers) {
            String policy = columnPolicies.getOrDefault(colName, "DEFAULT");

            switch (policy) {
                case "MASK":
                    // <MaskColumns> -> Redact with "*"
                    definition.setAttributeType(colName, AttributeType.IDENTIFYING_ATTRIBUTE);
                    break;
                case "ALLOW":
                    // <AllowOnlyColumns> -> Keep as-is
                    definition.setAttributeType(colName, AttributeType.INSENSITIVE_ATTRIBUTE);
                    break;
                default:
                    // // If not mentioned in XML, we treat it as a Quasi-Identifier (Safe Default)
                    // definition.setAttributeType(colName, AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
                    break;
            }
        }

        // Configure Privacy Model (k-Anonymity)
        ARXConfiguration config = ARXConfiguration.create();
        config.addPrivacyModel(new KAnonymity(2)); // Minimal k=2 for on-the-fly speed
        config.setSuppressionLimit(0.1); // Allow 10% row suppression to meet privacy goals

        // Run Anonymization
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXResult result = anonymizer.anonymize(data, config);

        // Format Output (Converting back to a readable string/JSON)
        return result.getOutput().toString();
    }

    /**
     * Logic to parse the XML instance and return a Map of Column -> Privacy Action
     */
    private Map<String, String> parseXMLRules(String xmlPath, String role, String tableName) throws Exception {
        Map<String, String> policies = new HashMap<>();
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder().parse(new File(xmlPath));

        // Find correct RuleSet for the Role
        NodeList ruleSets = doc.getElementsByTagName("RuleSet");
        Element targetRuleSet = null;
        for (int i = 0; i < ruleSets.getLength(); i++) {
            Element rs = (Element) ruleSets.item(i);
            if (rs.getAttribute("role").equalsIgnoreCase(role)) {
                targetRuleSet = rs;
                break;
            }
        }

        if (targetRuleSet == null) return policies;

        // Find TableRule
        NodeList tableRules = targetRuleSet.getElementsByTagName("TableRule");
        for (int i = 0; i < tableRules.getLength(); i++) {
            Element tr = (Element) tableRules.item(i);
            if (tr.getAttribute("table").equalsIgnoreCase(tableName)) {
                
                // Process Masked Columns
                addColsToMap(tr, "MaskColumns", "MASK", policies);
                
                // Process Allowed Columns
                addColsToMap(tr, "AllowOnlyColumns", "ALLOW", policies);
            }
        }
        return policies;
    }

    private void addColsToMap(Element parent, String tag, String type, Map<String, String> map) {
        NodeList list = parent.getElementsByTagName(tag);
        if (list.getLength() > 0) {
            NodeList columns = ((Element) list.item(0)).getElementsByTagName("Column");
            for (int i = 0; i < columns.getLength(); i++) {
                map.put(columns.item(i).getTextContent().trim(), type);
            }
        }
    }

    // MAIN METHOD FOR TESTING
    public static void main(String[] args) {
        try {
            ARXPrivacyAPI api = new ARXPrivacyAPI();

            // Mock Data (SQL Result Set)
            // List<String[]> data = new ArrayList<>();
            // data.add(new String[]{"id", "pt_Address", "payment_amount", "diagnosis"});
            // data.add(new String[]{"1", "123 Main St", "500", "Flu"});
            // data.add(new String[]{"2", "456 Oak St", "1200", "Cold"});
            // data.add(new String[]{"3", "123 Main St", "300", "Flu"}); // Duplicate address for k-anonymity

            // // Run for 'nurse' role on 'Patients' table
            // String output = api.anonymizeOnTheFly("nurse", "Patients", data, "XML/privacy_rules.xml");
            
            // System.out.println("--- ANONYMIZED OUTPUT ---");
            // System.out.println(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}