package com.quartz

import com.quartz.model.Loader
import com.quartz.model.person.Candidate
import com.quartz.model.person.Company
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions

class LoaderTest {
    @Test
    void testLoadCompanies() {

        List<Company> companies;

        companies = Loader.loadCompanies()

        int expectedSize = 5


        Assertions.assertEquals(expectedSize, companies.size())

        println("Load Companies test executed")

    }

    @Test
    void testLoadCandidates() {
        List<Candidate> candidates;

        candidates= Loader.loadCandidates()

        int expectedSize = 5

        Assertions.assertEquals(expectedSize, candidates.size())

        println("Load Candidates test executed")
    }
}
