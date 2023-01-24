package codeverse.brzodolokacije.ui.search.data

import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.ui.search.model.Category


object CategoriesRepository {

    fun getCategoriesData(): List<Category> {
        return listOf(
            Category(
                id = 0,
                name = "Datum",
                image = R.drawable.calendar_month_40_filled//"https://raw.githubusercontent.com/hitanshu-dhawan/McCompose/main/app/src/main/res/drawable-nodpi/" + "category_burgers.png"
            ),
            Category(
                id = 2,
                name = "Ocena",
                image = R.drawable.star_half_40//"https://raw.githubusercontent.com/hitanshu-dhawan/McCompose/main/app/src/main/res/drawable-nodpi/" + "category_fries.png"
            ),
            Category(
                id = 1,
                name = "Broj komentara",
                image = R.drawable.forum_40_outlined//"https://raw.githubusercontent.com/hitanshu-dhawan/McCompose/main/app/src/main/res/drawable-nodpi/" + "category_beverages.png"
            )
        )
    }

}