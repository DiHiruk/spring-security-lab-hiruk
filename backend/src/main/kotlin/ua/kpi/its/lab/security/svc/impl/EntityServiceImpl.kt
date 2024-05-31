package ua.kpi.its.lab.security.svc.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ua.kpi.its.lab.security.dto.HospitalRequest
import ua.kpi.its.lab.security.dto.HospitalResponse
import ua.kpi.its.lab.security.dto.MedicineResponse
import ua.kpi.its.lab.security.entity.Hospital
import ua.kpi.its.lab.security.entity.Medicine
import ua.kpi.its.lab.security.repo.HospitalRepository
import ua.kpi.its.lab.security.svc.HospitalService
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class EntityServiceImpl @Autowired constructor(
    private val repository: HospitalRepository
) : HospitalService {
    override fun create(hospital: HospitalRequest): HospitalResponse {
        val newMedicine = Medicine(
            name = hospital.medicines.name,
            medForm = hospital.medicines.medForm,
            manufacturer = hospital.medicines.manufacturer,
            manufactureDate = this.stringToDate(hospital.medicines.manufactureDate),
            shelfLife = this.stringToDate(hospital.medicines.shelfLife),
            price = this.stringToPrice(hospital.medicines.price),
            isPrescription = hospital.medicines.isPrescription,
            hospital = null // This will be set later
        )
        var newHospital = Hospital(
            name = hospital.name,
            address = hospital.address,
            profile = hospital.profile,
            openDate = this.stringToDate(hospital.openDate),
            departments = hospital.departments,
            place = hospital.place,
            isChild = hospital.isChild,
            medicines = newMedicine
        )
        newMedicine.hospital = newHospital
        newHospital = this.repository.save(newHospital)
        return this.hospitalEntityToDto(newHospital)
    }

    override fun read(): List<HospitalResponse> {
        return this.repository.findAll().map(this::hospitalEntityToDto)
    }

    override fun readById(id: Long): HospitalResponse {
        val hospital = this.getHospitalById(id)
        return this.hospitalEntityToDto(hospital)
    }

    override fun updateById(id: Long, hospital: HospitalRequest): HospitalResponse {
        val oldHospital = this.getHospitalById(id)
        val newMedicine = Medicine(
            name = hospital.medicines.name,
            medForm = hospital.medicines.medForm,
            manufacturer = hospital.medicines.manufacturer,
            manufactureDate = this.stringToDate(hospital.medicines.manufactureDate),
            shelfLife = this.stringToDate(hospital.medicines.shelfLife),
            price = this.stringToPrice(hospital.medicines.price),
            isPrescription = hospital.medicines.isPrescription,
            hospital = oldHospital
        )
        oldHospital.apply {
            name = hospital.name
            address = hospital.address
            profile = hospital.profile
            openDate = this@EntityServiceImpl.stringToDate(hospital.openDate)
            departments = hospital.departments
            place = hospital.place
            isChild = hospital.isChild
            medicines = newMedicine
        }
        val updatedHospital = this.repository.save(oldHospital)
        return this.hospitalEntityToDto(updatedHospital)
    }

    override fun deleteById(id: Long): HospitalResponse {
        val hospital = this.getHospitalById(id)
        this.repository.delete(hospital)
        return this.hospitalEntityToDto(hospital)
    }

    private fun getHospitalById(id: Long): Hospital {
        return this.repository.findById(id).getOrElse {
            throw IllegalArgumentException("Hospital not found by id = $id")
        }
    }

    private fun hospitalEntityToDto(hospital: Hospital): HospitalResponse {
        return HospitalResponse(
            id = hospital.id,
            name = hospital.name,
            address = hospital.address,
            profile = hospital.profile,
            openDate = this.dateToString(hospital.openDate),
            departments = hospital.departments,
            place = hospital.place,
            isChild = hospital.isChild,
            medicines = this.medicineEntityToDto(hospital.medicines)
        )
    }

    private fun medicineEntityToDto(medicine: Medicine): MedicineResponse {
        return MedicineResponse(
            id = medicine.id,
            name = medicine.name,
            medForm = medicine.medForm,
            manufacturer = medicine.manufacturer,
            manufactureDate = this.dateToString(medicine.manufactureDate),
            shelfLife = this.dateToString(medicine.shelfLife),
            price = this.priceToString(medicine.price),
            isPrescription = medicine.isPrescription
        )
    }

    private fun dateToString(date: Date): String {
        val instant = date.toInstant()
        val dateTime = instant.atOffset(ZoneOffset.UTC).toLocalDateTime()
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME)
    }

    private fun stringToDate(date: String): Date {
        return try {
            val dateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
            val instant = dateTime.toInstant(ZoneOffset.UTC)
            Date.from(instant)
        } catch (e: Exception) {
            Date() // Return current date as fallback
        }
    }

    private fun priceToString(price: BigDecimal): String = price.toString()

    private fun stringToPrice(price: String): BigDecimal {
        return try {
            BigDecimal(price)
        } catch (e: Exception) {
            BigDecimal.ZERO
        }
    }
}
