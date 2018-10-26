package service

import de.pengelkes.jooq.model.tables.Penalties
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
        return Jooq.instance.select().from(Penalties.PENALTIES).fetch()
    }
}