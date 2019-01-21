package services

import de.pengelkes.jooq.model.tables.Penalties.PENALTIES

class PenaltyService private constructor() {
    private object Holder {
        val OBJECT = PenaltyService()
    }

    companion object {
        val instance: PenaltyService by lazy { Holder.OBJECT }
    }

    fun getAll(): List<PenaltyModel> {
        return Jooq.instance.select().from(PENALTIES).fetchInto(PenaltyModel::class.java)
    }

    fun getByName(name: String): PenaltyModel {
        return Jooq.instance.select()
                .from(PENALTIES)
                .where(PENALTIES.PENALTY_NAME.eq(name))
                .fetchOneInto(PenaltyModel::class.java)
    }
}

data class PenaltyModel(val id: Int, val penaltyName: String, val penaltyCost: Int, val caseOfBeerCost: Int)