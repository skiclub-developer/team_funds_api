import de.pengelkes.jooq.model.tables.Users
import io.javalin.Context
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import services.*

val DB_USER = "TEAM_FUNDS_DB_USER"
val DB_PASSWORD = "TEAM_FUNDS_DB_PASSWORD"
val DB_URL = "TEAM_FUNDS_DB_URL"

fun main(args: Array<String>) {
    val app = Javalin.create()
    app.enableCorsForAllOrigins()
    app.enableCaseSensitiveUrls()

    app.routes {
        path("user") {
            //get all users or filtered by userType / name
            get {
                if (it.queryParam("userType") != null) {
                    it.json(UserService.instance.getByUserType(it.queryParam("userType")!!))
                } else if (it.queryParam("name") != null) {
                    val user = UserService.instance.getByName(it.queryParam("name")!!)
                    if (user != null) {
                        it.json(user)
                    } else {
                        it.status(404).json("Not Found")
                    }
                } else {
                    it.json(UserService.instance.getAll())
                }
            }

            //update User
            patch {
                UserService.instance.updateUser(it.body<UserModel>())
            }

            //let user pay his penalties
            path(":user_id") {
                path("pay") {
                    patch {
                        val shouldPay = it.shouldPay();
                        if (shouldPay != null) {
                            if (shouldPay.second == "CURRENT_PENALTIES") {
                                UserService.instance.pay(
                                        shouldPay.third,
                                        shouldPay.first,
                                        Users.USERS.CURRENT_PENALTIES
                                )
                            } else {
                                UserService.instance.pay(
                                        shouldPay.third,
                                        shouldPay.first,
                                        Users.USERS.CASE_OF_BEER
                                )
                            }
                        }
                    }
                }

                //add penalty to given user
                path("addPenalty") {
                    patch {
                        val shouldPay = it.shouldPay()
                        if (shouldPay != null) {
                            if (shouldPay.second == "CURRENT_PENALTIES") {
                                UserService.instance.addPenalty(
                                        shouldPay.third,
                                        shouldPay.first,
                                        Users.USERS.CURRENT_PENALTIES
                                )
                            } else {
                                UserService.instance.addPenalty(
                                        shouldPay.third,
                                        shouldPay.first,
                                        Users.USERS.CASE_OF_BEER
                                )
                            }
                        }
                    }
                }

            }
        }

        path("penalties") {
            //get all penalties or filtered by name
            get {
                if (it.queryParam("name") != null) {
                    it.json(PenaltyService.instance.getByName(it.queryParam("name")!!))
                } else {
                    it.json(PenaltyService.instance.getAll())
                }
            }
        }

        path("userPenaltyBeerPayment") {
            //let user pay for beer
            post {
                UserPenaltyBeerPaymentService.instance.pay(it.body<UserPenaltyBeerPaymentModel>())
            }

            path(":user_id") {
                //get all beer payments by user
                get {
                    it.json(UserPenaltyBeerPaymentService.instance.getByUser(it.pathParam("user_id").toInt()))
                }
            }
        }

        path("userPenaltyPayment") {
            //let user pay for their penalties
            post {
                UserPenaltyPaymentService.instance.pay(it.body<UserPenaltyPaymentModel>())
            }

            path(":user_id") {
                //get all payments by user
                get {
                    it.json(UserPenaltyPaymentService.instance.getByUser(it.pathParam("user_id").toInt()))
                }
            }
        }

        path("userPenalty") {
            //add penalty for user
            post {
                UserPenaltyService.instance.createUserPenalty(it.body<UserPenaltyModel>())
            }

            path(":user_id") {
                //get all penalties by user
                get { it.json(UserPenaltyService.instance.getPenaltiesByUser(it.pathParam("user_id").toInt())) }

                path("penalty") {
                    path(":penalty_id") {
                        //get all penalties for a given type for a specific user
                        get {
                            val userPenalty = UserPenaltyService.instance.getUserPenalty(
                                    it.pathParam("user_id").toInt(),
                                    it.pathParam("penalty_id").toInt()
                            )

                            if (userPenalty.isNotEmpty()) {
                                it.json(userPenalty)
                            }
                        }
                    }
                }
            }
        }
    }

    app.start(8080)
}

fun Context.shouldPay(): Triple<Int, String, Int>? {
    if (this.queryParam("amount") != null && this.queryParam("type") != null) {
        return Triple(this.queryParam("amount")!!.toInt(), this.queryParam("type")!!,
                this.pathParam("user_id").toInt())
    }

    return null
}