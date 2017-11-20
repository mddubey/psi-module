package org.bahmni.module.bahmnipsi.identifier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.*;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;
import java.util.HashSet;

import static junit.framework.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Context.class)
public class PatientUICIdentifierTest {
    @Mock
    private ConceptService conceptService;

    @Mock
    private Concept concept;

    @Mock
    private ConceptName conceptName;

    @Test
    public void shouldReturnGeneratedUICIdentifier() {
        PatientUICIdentifier patientUICIdentifier = new PatientUICIdentifier();
        Patient patient = setUpPatientData();

        PowerMockito.mockStatic(Context.class);

        when(Context.getConceptService()).thenReturn(conceptService);
        when(conceptService.getConcept("1001")).thenReturn(concept);
        when(concept.getName()).thenReturn(conceptName);
        when(conceptName.getName()).thenReturn("Harare");

        patientUICIdentifier.updateUICIdentifier(patient);

        assertEquals("NEOERE130170M0", patient.getPatientIdentifier("UIC").getIdentifier());
    }

    private Patient setUpPatientData() {
        Patient patient = new Patient();

        PersonName personName = new PersonName();
        personName.setMiddleName("Doe");

        Date birthDate = new Date(1078884319);

        PatientIdentifierType patientIdentifierType = new PatientIdentifierType();
        patientIdentifierType.setName("UIC");
        PatientIdentifier patientIdentifier = new PatientIdentifier("100002", patientIdentifierType, new Location());
        HashSet<PatientIdentifier> patientIdentifiers = new HashSet<PatientIdentifier>();
        patientIdentifiers.add(patientIdentifier);

        PersonAttributeType personAttributeTypeMother = new PersonAttributeType();
        PersonAttributeType personAttributeTypeDistrict = new PersonAttributeType();
        personAttributeTypeMother.setName("Mother's name");
        personAttributeTypeDistrict.setName("District of Birth");
        PersonAttribute personMotherNameAttribute = new PersonAttribute(personAttributeTypeMother, "jaen Doe");
        PersonAttribute personDistrictAttribute = new PersonAttribute(personAttributeTypeDistrict, "1001");
        HashSet<PersonAttribute> personAttributes = new HashSet<PersonAttribute>();
        personAttributes.add(personMotherNameAttribute);
        personAttributes.add(personDistrictAttribute);

        patient.addName(personName);
        patient.setGender("M");
        patient.setBirthdate(birthDate);
        patient.setIdentifiers(patientIdentifiers);
        patient.setAttributes(personAttributes);
        return patient;
    }
}

