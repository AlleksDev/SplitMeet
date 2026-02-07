package com.coditos.splitmeet.features.outing.domain.entities

enum class SplitType(val value: String, val displayName: String) {
    EQUAL("equal", "División igual"),
    CUSTOM_FIXED("custom_fixed", "Monto fijo personalizado"),
    PER_CONSUMPTION("per_consumption", "Por consumo"),
    SINGLE_PAYER("single_payer", "Un solo pagador");

    companion object {
        fun fromValue(value: String): SplitType {
            return entries.find { it.value == value } ?: EQUAL
        }

        fun getAll(): List<SplitType> = entries.toList()
    }
}
