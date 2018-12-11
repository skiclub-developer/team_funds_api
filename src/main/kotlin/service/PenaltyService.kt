package service

import de.pengelkes.jooq.model.tables.Penalties.PENALTIES
import de.pengelkes.jooq.model.tables.records.PenaltiesRecord
import org.jooq.Record
import org.jooq.Result

class PenaltyService private constructor() {
    private object Holder {
        val OBJECT = PenaltyService()
    }

    companion object {
        val instance: PenaltyService by lazy { Holder.OBJECT }
    }

    fun getAll(): Result<Record> {
        return Jooq.instance.select().from(PENALTIES).fetch()
    }

    fun getByName(name: String): PenaltiesRecord {
        return Jooq.instance.select()
                .from(PENALTIES)
                .where(PENALTIES.PENALTY_NAME.eq(name))
                .fetchOneInto(PenaltiesRecord::class.java)
    }
}