package ru.itd.sarafan.businesslogic.interactors

import ru.itd.sarafan.rest.model.tags.Term

/**
 * Created by macbook on 23.10.17.
 */
class GetTagInteractor(tagTerm: Term?): GetInteractor<Term>(tagTerm)