package com.xzinoviou.guessthenumber.service

import com.xzinoviou.guessthenumber.dao.DatabaseDao
import spock.lang.Specification

/**
 * @author : Xenofon Zinoviou
 */
class PlayerServiceImplSpec extends Specification {

    private DatabaseDao databaseDao
    private PlayerServiceImpl testClass

    void setup() {
        databaseDao = Mock()
        testClass = new PlayerServiceImpl(databaseDao)
    }

    void cleanup() {
        testClass = null
        databaseDao = null
    }
}
