package ua.kpi.its.lab.security.svc

import ua.kpi.its.lab.security.dto.HospitalRequest
import ua.kpi.its.lab.security.dto.HospitalResponse

interface HospitalService {
    fun create(hospital: HospitalRequest): HospitalResponse
    fun read(): List<HospitalResponse>
    fun readById(id: Long): HospitalResponse
    fun updateById(id: Long, hospital: HospitalRequest): HospitalResponse
    fun deleteById(id: Long): HospitalResponse
}
