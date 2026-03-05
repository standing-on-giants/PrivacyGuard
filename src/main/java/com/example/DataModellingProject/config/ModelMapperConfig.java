package com.example.DataModellingProject.config;

import com.example.DataModellingProject.dto.*;
import com.example.DataModellingProject.model.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Converter<Patient, String> patientToName = ctx -> {
            Patient p = ctx.getSource();
            return p == null ? null : p.getFName() + " " + p.getLName();
        };

        Converter<Doctor, String> doctorToName = ctx -> {
            Doctor d = ctx.getSource();
            return d == null ? null : d.getFName() + " " + d.getLName();
        };

        Converter<Nurse, String> nurseToName = ctx -> {
            Nurse n = ctx.getSource();
            return n == null ? null : n.getFName() + " " + n.getLName();
        };

        Converter<Helper, String> helperToName = ctx -> {
            Helper h = ctx.getSource();
            return h == null ? null : h.getFName() + " " + h.getLName();
        };

        Converter<Department, String> deptToName = ctx -> {
            Department d = ctx.getSource();
            return d == null ? null : d.getDeptName();
        };

        Converter<Room, Integer> roomToNo = ctx -> {
            Room r = ctx.getSource();
            return r == null ? null : r.getRoomNo();
        };

        // --- 3. Attach Converters to Specific DTOs ---

        // Master Data Mappings
        modelMapper.typeMap(Nurse.class, NurseSearchResponse.class).addMappings(mapper -> {
            mapper.using(deptToName).map(Nurse::getDepartment, NurseSearchResponse::setDeptName);
        });

        modelMapper.typeMap(Helper.class, HelperSearchResponse.class).addMappings(mapper -> {
            mapper.using(deptToName).map(Helper::getDepartment, HelperSearchResponse::setDeptName);
        });

        modelMapper.typeMap(Doctor.class, DoctorSearchResponse.class).addMappings(mapper -> {
            mapper.using(deptToName).map(Doctor::getDepartment, DoctorSearchResponse::setDeptName);
            mapper.using(roomToNo).map(Doctor::getOffice, DoctorSearchResponse::setRoomNo);
        });

        modelMapper.typeMap(MedicalRecord.class, MedicalRecordSearchResponse.class).addMappings(mapper -> {
            mapper.using(patientToName).map(MedicalRecord::getPatient, MedicalRecordSearchResponse::setPatientName);
            mapper.using(doctorToName).map(MedicalRecord::getDoctor, MedicalRecordSearchResponse::setDoctorName);
        });

        modelMapper.typeMap(SurgeryRecord.class, SurgeryRecordSearchResponse.class).addMappings(mapper -> {
            mapper.using(patientToName).map(SurgeryRecord::getPatient, SurgeryRecordSearchResponse::setPatientName);
            mapper.using(doctorToName).map(SurgeryRecord::getSurgeon, SurgeryRecordSearchResponse::setSurgeonName);
            mapper.using(roomToNo).map(SurgeryRecord::getRoom, SurgeryRecordSearchResponse::setRoomNo);
        });

        modelMapper.typeMap(Appointment.class, AppointmentSearchResponse.class).addMappings(mapper -> {
            mapper.using(patientToName).map(Appointment::getPatient, AppointmentSearchResponse::setPatientName);
            mapper.using(doctorToName).map(Appointment::getDoctor, AppointmentSearchResponse::setDoctorName);
        });

        modelMapper.typeMap(StaffShift.class, StaffShiftSearchResponse.class).addMappings(mapper -> {
            mapper.using(doctorToName).map(StaffShift::getDoctor, StaffShiftSearchResponse::setDoctorName);
            mapper.using(nurseToName).map(StaffShift::getNurse, StaffShiftSearchResponse::setNurseName);
            mapper.using(helperToName).map(StaffShift::getHelper, StaffShiftSearchResponse::setHelperName);
        });

        modelMapper.typeMap(BedRecord.class, BedRecordSearchResponse.class).addMappings(mapper -> {
            mapper.using(patientToName).map(BedRecord::getPatient, BedRecordSearchResponse::setPatientName);
            mapper.using(nurseToName).map(BedRecord::getNurse, BedRecordSearchResponse::setNurseName);
        });

        modelMapper.typeMap(RoomRecord.class, RoomRecordSearchResponse.class).addMappings(mapper -> {
            mapper.using(patientToName).map(RoomRecord::getPatient, RoomRecordSearchResponse::setPatientName);
            mapper.using(nurseToName).map(RoomRecord::getNurse, RoomRecordSearchResponse::setNurseName);
            mapper.using(roomToNo).map(RoomRecord::getRoom, RoomRecordSearchResponse::setRoomNo);
        });

        return modelMapper;
    }
}