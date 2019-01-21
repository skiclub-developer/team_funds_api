package services

import de.pengelkes.jooq.model.tables.Penalties.PENALTIES
import de.pengelkes.jooq.model.tables.UserPenalties.USER_PENALTIES
import java.sql.Timestamp

class UserPenaltyService private constructor() {
    private object Holder {
        val OBJECT = UserPenaltyService()
    }

    companion object {
        val ROUTE = "userPenalty"
        val instance: UserPenaltyService by lazy { Holder.OBJECT }
    }

    fun getUserPenalty(userId: Int, penaltyId: Int): List<UserPenaltyModel> {
        return Jooq.instance.select().from(USER_PENALTIES)
                .where(USER_PENALTIES.USER_ID.eq(userId))
                .and(USER_PENALTIES.PENALTY_ID.eq(penaltyId)).fetchInto(UserPenaltyModel::class.java)

    }

    fun getPenaltiesByUser(userId: Int): List<UserPenaltyModel> {
        return Jooq.instance.select().from(USER_PENALTIES)
                .join(PENALTIES).on(USER_PENALTIES.PENALTY_ID.eq(PENALTIES.ID))
                .where(USER_PENALTIES.USER_ID.eq(userId))
                .orderBy(USER_PENALTIES.PENALTY_ID, USER_PENALTIES.CREATED_AT.desc())
                .fetchInto(UserPenaltyModel::class.java)

    }

    fun createUserPenalty(userPenaltyModel: UserPenaltyModel) {
        Jooq.instance.insertInto(USER_PENALTIES)
                .set(USER_PENALTIES.AMOUNT, userPenaltyModel.amount)
                .set(USER_PENALTIES.USER_ID, userPenaltyModel.userId)
                .set(USER_PENALTIES.PENALTY_ID, userPenaltyModel.penaltyId)
                .set(USER_PENALTIES.CREATED_AT, Timestamp(System.currentTimeMillis()))
                .set(USER_PENALTIES.CHANGED_BY, userPenaltyModel.changedBy)
                .execute()
    }
}

open class UserPenaltyModel(val id: Int = -1, val userId: Int, val penaltyId: Int, val amount: Int,
                            val createdAt: Timestamp = Timestamp(System.currentTimeMillis()), val changedBy: String,
                            val penaltyName: String = "")
