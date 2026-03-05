package com.example.DataModellingProject.specification;

import com.example.DataModellingProject.model.Doctor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class DoctorSpecification {

    public static Specification<Doctor> hasFNameIn(List<String> fNames) {
        return (root, query, criteriaBuilder) -> {
            if (fNames == null || fNames.isEmpty()) {
                return null;
            }
            return root.get("fName").in(fNames);
        };
    }

    public static Specification<Doctor> hasLNameIn(List<String> lNames) {
        return (root, query, criteriaBuilder) -> {
            if (lNames == null || lNames.isEmpty()) {
                return null;
            }
            return root.get("lName").in(lNames);
        };
    }

    public static Specification<Doctor> hasGenderIn(List<String> genders) {
        return (root, query, criteriaBuilder) -> {
            if (genders == null || genders.isEmpty()) {
                return null;
            }
            return root.get("gender").in(genders);
        };
    }

    public static Specification<Doctor> hasSurgeonTypeIn(List<String> surgeonTypes) {
        return (root, query, criteriaBuilder) -> {
            if (surgeonTypes == null || surgeonTypes.isEmpty()) {
                return null;
            }
            return root.get("surgeonType").in(surgeonTypes);
        };
    }

    public static Specification<Doctor> hasDepartmentNameIn(List<String> deptNames) {
        return (root, query, criteriaBuilder) -> {
            if (deptNames == null || deptNames.isEmpty()) {
                return null;
            }
            return root.join("department").get("deptName").in(deptNames);
        };
    }

    public static Specification<Doctor> hasRoomNoIn(List<Integer> roomNos) {
        return (root, query, criteriaBuilder) -> {
            if (roomNos == null || roomNos.isEmpty()) {
                return null;
            }
            return root.join("office").get("roomNo").in(roomNos);
        };
    }
}