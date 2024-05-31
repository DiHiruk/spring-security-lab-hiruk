package ua.kpi.its.lab.security.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "hospitals")
class Hospital(
    @Column
    var name: String,

    @Column
    var address: String,

    @Column
    var profile: String,

    @Column
    var openDate: Date,

    @Column
    var departments: Int,

    @Column
    var place: Int,

    @Column
    var isChild: Boolean,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "medicine_id", referencedColumnName = "id")
    var medicines: Medicine
) : Comparable<Hospital> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = -1

    override fun compareTo(other: Hospital): Int {
        val equal = this.name == other.name && this.openDate.time == other.openDate.time
        return if (equal) 0 else 1
    }

    override fun toString(): String {
        return "Hospital(name=$name, openDate=$openDate, medicines=$medicines)"
    }
}

@Entity
@Table(name = "medicines")
class Medicine(
    @Column
    var name: String,

    @Column
    var medForm: String,

    @Column
    var manufacturer: String,

    @Column
    var manufactureDate: Date,

    @Column
    var shelfLife: Date,

    @Column
    var price: BigDecimal,

    @Column
    var isPrescription: Boolean,

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    var hospital: Hospital? = null
) : Comparable<Medicine> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = -1

    override fun compareTo(other: Medicine): Int {
        val equal = this.name == other.name && this.shelfLife.time == other.shelfLife.time
        return if (equal) 0 else 1
    }

    override fun toString(): String {
        return "Medicine(name=$name, shelfLife=$shelfLife)"
    }
}
