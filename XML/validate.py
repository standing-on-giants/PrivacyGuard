import xmlschema

print("Loading schema...")
schema = xmlschema.XMLSchema11('privacy_rules_withARX.xsd')

print("Validating XML...")
try:
    schema.validate('privacy_rules_withARX.xml')
    print("XML is VALID!")
except xmlschema.XMLSchemaException as e:
    print("Validation FAILED:")
    print(e)