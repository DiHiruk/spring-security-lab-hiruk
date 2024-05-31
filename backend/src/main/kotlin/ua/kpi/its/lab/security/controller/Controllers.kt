package ua.kpi.its.lab.security.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.web.bind.annotation.*
import ua.kpi.its.lab.security.dto.HospitalRequest
import ua.kpi.its.lab.security.dto.HospitalResponse
import ua.kpi.its.lab.security.svc.HospitalService
import java.time.Instant

@RestController
@RequestMapping("/hospitals")
class HospitalController @Autowired constructor(
    private val hospitalService: HospitalService
) {
    @GetMapping(path = ["", "/"])
    fun hospitals(): List<HospitalResponse> = hospitalService.read()

    @GetMapping("{id}")
    fun readHospital(@PathVariable("id") id: Long): ResponseEntity<HospitalResponse> {
        return wrapNotFound { hospitalService.readById(id) }
    }

    @PostMapping(path = ["", "/"])
    fun createHospital(@RequestBody hospital: HospitalRequest): HospitalResponse {
        return hospitalService.create(hospital)
    }

    @PutMapping("{id}")
    fun updateHospital(
        @PathVariable("id") id: Long,
        @RequestBody hospital: HospitalRequest
    ): ResponseEntity<HospitalResponse> {
        return wrapNotFound { hospitalService.updateById(id, hospital) }
    }

    @DeleteMapping("{id}")
    fun deleteHospital(@PathVariable("id") id: Long): ResponseEntity<HospitalResponse> {
        return wrapNotFound { hospitalService.deleteById(id) }
    }

    fun <T> wrapNotFound(call: () -> T): ResponseEntity<T> {
        return try {
            val result = call()
            ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }
}
@RestController
@RequestMapping("/auth")
class AuthenticationTokenController @Autowired constructor(
    private val encoder: JwtEncoder
) {
    private val authTokenExpiry: Long = 3600L // in seconds

    @PostMapping("token")
    fun token(auth: Authentication): String {
        val now = Instant.now()
        val scope = auth
            .authorities
            .joinToString(" ", transform = GrantedAuthority::getAuthority)
        val claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(authTokenExpiry))
            .subject(auth.name)
            .claim("scope", scope)
            .build()
        return encoder.encode(JwtEncoderParameters.from(claims)).tokenValue
    }
}