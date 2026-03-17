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

        Converter<Department, String> deptToName = ctx -> {
            Department d = ctx.getSource();
            return d == null ? null : d.getDeptName();
        };

        Converter<Room, Integer> roomToNo = ctx -> {
            Room r = ctx.getSource();
            return r == null ? null : r.getRoomNo();
        };

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
            mapper.map(src -> src.getPatient().getFName(), MedicalRecordSearchResponse::setPatientFirstName);
            mapper.map(src -> src.getPatient().getLName(), MedicalRecordSearchResponse::setPatientLastName);
            mapper.map(src -> src.getDoctor().getFName(), MedicalRecordSearchResponse::setDoctorFirstName);
            mapper.map(src -> src.getDoctor().getLName(), MedicalRecordSearchResponse::setDoctorLastName);
        });

        modelMapper.typeMap(SurgeryRecord.class, SurgeryRecordSearchResponse.class).addMappings(mapper -> {
            mapper.map(src -> src.getPatient().getFName(), SurgeryRecordSearchResponse::setPatientFirstName);
            mapper.map(src -> src.getPatient().getLName(), SurgeryRecordSearchResponse::setPatientLastName);
            mapper.map(src -> src.getSurgeon().getFName(), SurgeryRecordSearchResponse::setSurgeonFirstName);
            mapper.map(src -> src.getSurgeon().getLName(), SurgeryRecordSearchResponse::setSurgeonLastName);
            mapper.using(roomToNo).map(SurgeryRecord::getRoom, SurgeryRecordSearchResponse::setRoomNo);
        });

        modelMapper.typeMap(Appointment.class, AppointmentSearchResponse.class).addMappings(mapper -> {
            mapper.map(src -> src.getPatient().getFName(), AppointmentSearchResponse::setPatientFirstName);
            mapper.map(src -> src.getPatient().getLName(), AppointmentSearchResponse::setPatientLastName);
            mapper.map(src -> src.getDoctor().getFName(), AppointmentSearchResponse::setDoctorFirstName);
            mapper.map(src -> src.getDoctor().getLName(), AppointmentSearchResponse::setDoctorLastName);
        });

        modelMapper.typeMap(StaffShift.class, StaffShiftSearchResponse.class).addMappings(mapper -> {
            mapper.map(src -> src.getDoctor().getFName(), StaffShiftSearchResponse::setDoctorFirstName);
            mapper.map(src -> src.getDoctor().getLName(), StaffShiftSearchResponse::setDoctorLastName);
            mapper.map(src -> src.getNurse().getFName(), StaffShiftSearchResponse::setNurseFirstName);
            mapper.map(src -> src.getNurse().getLName(), StaffShiftSearchResponse::setNurseLastName);
            mapper.map(src -> src.getHelper().getFName(), StaffShiftSearchResponse::setHelperFirstName);
            mapper.map(src -> src.getHelper().getLName(), StaffShiftSearchResponse::setHelperLastName);
        });

        modelMapper.typeMap(BedRecord.class, BedRecordSearchResponse.class).addMappings(mapper -> {
            mapper.map(src -> src.getPatient().getFName(), BedRecordSearchResponse::setPatientFirstName);
            mapper.map(src -> src.getPatient().getLName(), BedRecordSearchResponse::setPatientLastName);
            mapper.map(src -> src.getNurse().getFName(), BedRecordSearchResponse::setNurseFirstName);
            mapper.map(src -> src.getNurse().getLName(), BedRecordSearchResponse::setNurseLastName);
            mapper.map(src -> src.getNurse().getFName(), BedRecordSearchResponse::setHelperFirstName);
            mapper.map(src -> src.getNurse().getLName(), BedRecordSearchResponse::setHelperLastName);
        });

        modelMapper.typeMap(RoomRecord.class, RoomRecordSearchResponse.class).addMappings(mapper -> {
            mapper.map(src -> src.getPatient().getFName(), RoomRecordSearchResponse::setPatientFirstName);
            mapper.map(src -> src.getPatient().getLName(), RoomRecordSearchResponse::setPatientLastName);
            mapper.map(src -> src.getNurse().getFName(), RoomRecordSearchResponse::setNurseFirstName);
            mapper.map(src -> src.getNurse().getLName(), RoomRecordSearchResponse::setNurseLastName);
            mapper.using(roomToNo).map(RoomRecord::getRoom, RoomRecordSearchResponse::setRoomNo);
        });

        return modelMapper;
    }
}