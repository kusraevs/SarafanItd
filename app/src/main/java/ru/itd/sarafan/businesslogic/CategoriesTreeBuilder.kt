package ru.itd.sarafan.businesslogic

import io.reactivex.functions.Function
import ru.itd.sarafan.rest.model.Category

/**
 * Created by macbook on 20.10.17.
 */
class CategoriesTreeBuilder: Function<List<Category>, List<Category>> {

  override fun apply(allCategories: List<Category>): List<Category> {
    val rootList = arrayListOf(allCategories[0].copy(id = 0))
    //val rootList = allCategories.filter { it.id == 0 }.takeLast(1)
    return updateChildCategories(rootList, allCategories)[0].childs.drop(1)
  }


  private fun updateChildCategories(categories: List<Category>, allCategories: List<Category>): List<Category>{
    return categories.map { category ->
      val childCategories = allCategories.filter { it.parent == category.id }

      val newChildCategories = if (childCategories.isNotEmpty())
        updateChildCategories(childCategories, allCategories)
      else
        childCategories
      val sortedCategories = newChildCategories.sortedWith(Comparator { c1, c2 -> c1.id - c2.id})
      return@map category.copy(childs = sortedCategories)
    }
  }
}