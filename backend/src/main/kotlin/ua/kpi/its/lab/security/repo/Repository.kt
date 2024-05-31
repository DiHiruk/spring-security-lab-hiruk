package ua.kpi.its.lab.security.repo

import org.springframework.data.jpa.repository.JpaRepository
import ua.kpi.its.lab.security.entity.Hospital
import ua.kpi.its.lab.security.entity.Medicine

interface HospitalRepository : JpaRepository<Hospital, Long> {
}

interface MedicineRepository : JpaRepository<Medicine, Long> {
}
