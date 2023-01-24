package codeverse.brzodolokacije.utils.helpers

import java.math.BigDecimal
import java.math.RoundingMode

class NumberHelper {

    companion object{

        fun roundDoubleNumber(num: Double) : Double {

            val decimal = BigDecimal(num).setScale(1, RoundingMode.HALF_EVEN)

            return decimal.toDouble()
        }

        fun roundDoubleNumberToThreeDecimals(num: Double) : Double {

            val decimal = BigDecimal(num).setScale(3, RoundingMode.HALF_EVEN)

            return decimal.toDouble()
        }
    }
}