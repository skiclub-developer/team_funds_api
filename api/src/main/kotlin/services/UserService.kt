package services

import de.pengelkes.jooq.model.tables.Users.USERS
import de.pengelkes.jooq.model.tables.records.UsersRecord
import org.jooq.TableField

class UserService private constructor() {
    private object Holder {
        val OBJECT = UserService()
    }

    companion object {
        val instance: UserService by lazy { Holder.OBJECT }
    }

    fun getAll(): List<UserModel> {
        return Jooq.instance.select().from(USERS).fetchInto(UserModel::class.java)
    }

    fun getByUserType(userType: String): List<UserModel> {
        return Jooq.instance.select()
                .from(USERS)
                .where(USERS.TYPE.eq(UserType.valueOf(userType)))
                .fetchInto(UserModel::class.java)
    }

    fun updateUser(userModel: UserModel) {
        Jooq.instance.update(USERS)
                .set(USERS.TYPE, userModel.type)
                .set(USERS.CURRENT_PENALTIES, userModel.currentPenalties)
                .set(USERS.NAME, userModel.name)
                .set(USERS.CASE_OF_BEER, userModel.caseOfBeer)
                .where(USERS.ID.eq(userModel.id))
                .execute()
    }

    fun pay(id: Int, amount: Int, field: TableField<UsersRecord, Int>) {
        Jooq.instance.update(USERS)
                .set(field, field - amount)
                .where(USERS.ID.eq(id))
                .execute()
    }

    fun addPenalty(id: Int, amount: Int, field: TableField<UsersRecord, Int>) {
        Jooq.instance.update(USERS)
                .set(field, field + amount)
                .where(USERS.ID.eq(id))
                .execute()
    }

    fun getByName(name: String): UserModel? {
        val record = Jooq.instance.select().from(USERS).where(USERS.NAME.equalIgnoreCase(name)).fetchOne()
        if (record != null) {
            return record.into(UserModel::class.java)
        }

        return null
    }
}

data class UserModel(val id: Int = -1, val name: String, val currentPenalties: Int,
                     val caseOfBeer: Int, val type: UserType)