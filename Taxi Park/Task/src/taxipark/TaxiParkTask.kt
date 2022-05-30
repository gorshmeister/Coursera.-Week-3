package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
    allDrivers - trips.map { it.driver }.toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> {
    val map = mutableMapOf<Passenger, Int>()
    val set = mutableSetOf<Passenger>()

    trips.forEach() {
        it.passengers.forEach() {
            if (map.containsKey(it))
                map += it to map.getValue(it).plus(1)
            else map += it to 1
        }
    }
    map.forEach() {
        if (minTrips == 0)
            set.addAll(allPassengers)
        else if (it.value >= minTrips)
            set += it.key
    }
    return set
}

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> {
    val set = mutableSetOf<Passenger>()
    val map = mutableMapOf<Passenger, Int>()

    trips.forEach() {
        if (it.driver == driver)
            it.passengers.forEach() {
                if (map.containsKey(it)) {
                    map += it to map.getValue(it).plus(1)
                } else map += it to 1
            }
    }
    map.forEach {
        if (it.value > 1)
            set += it.key
    }

    return set
}

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    val mapWithDiscount = mutableMapOf<Passenger, Int>()
    trips.partition { it.discount != null }.first.forEach() {
        it.passengers.forEach() {
            mapWithDiscount += it to (mapWithDiscount[it]?.plus(1) ?: 1)
        }
    }

    val mapWithoutDiscount = mutableMapOf<Passenger, Int>()
    trips.partition { it.discount != null }.second.forEach() {
        it.passengers.forEach() {
            mapWithoutDiscount += it to (mapWithoutDiscount[it]?.plus(1) ?: 1)
        }
    }

    val set = mutableSetOf<Passenger>()
    mapWithDiscount.forEach() {
        if (mapWithoutDiscount.containsKey(it.key) && it.value > mapWithoutDiscount.get(it.key)!!)
            set += it.key
        else if (!mapWithoutDiscount.containsKey(it.key))
            set += it.key
    }

    return set
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    if (trips.isEmpty()) return null

    val map = mutableMapOf<IntRange, Int>()

    val durationsList = trips.map { it.duration }.sorted()
    durationsList.forEach() {
        val start = (it / 10) * 10
        val end = start + 9
        val range = start..end

        if (it in range)
            map += range to (map[range]?.plus(1) ?: 1)
    }

    return map.maxBy { it.value }!!.key

}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    val map = mutableMapOf<Driver, Double>()
    allDrivers.forEach() { map += it to 0.0 }

    val income = trips.forEach() { map += it.driver to (map[it.driver]?.plus(it.cost) ?: 0.0) }
    val incomeList = map.map { it.value }.sorted().toMutableList()

    var incomeTotal = 0.0
    map.forEach() { incomeTotal += it.value }

    val numOfDriversFrom20: Int = (allDrivers.count() * 0.2).toInt()

    var amountOfDriversIncomeFrom20 = 0.0
    for (i in 0 until numOfDriversFrom20) {
        amountOfDriversIncomeFrom20 += incomeList.last()
        incomeList.removeAt(incomeList.size - 1)
    }

    val pareto = amountOfDriversIncomeFrom20 / incomeTotal

    return pareto >= 0.8

}