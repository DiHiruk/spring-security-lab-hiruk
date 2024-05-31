package ua.kpi.its.lab.security.dto

data class HospitalRequest(
    var name: String,
    var address: String,
    var profile: String,
    var openDate: String,
    var departments: Int,
    var place: Int,
    var isChild: Boolean,
    var medicines: MedicineRequest
)

data class HospitalResponse(
    var id: Long,
    var name: String,
    var address: String,
    var profile: String,
    var openDate: String,
    var departments: Int,
    var place: Int,
    var isChild: Boolean,
    var medicines: MedicineResponse
)

data class MedicineRequest(
    var name: String,
    var medForm: String,
    var manufacturer: String,
    var manufactureDate: String,
    var shelfLife: String,
    var price: String,
    var isPrescription: Boolean
)

data class MedicineResponse(
    var id: Long,
    var name: String,
    var medForm: String,
    var manufacturer: String,
    var manufactureDate: String,
    var shelfLife: String,
    var price: String,
    var isPrescription: Boolean
)
