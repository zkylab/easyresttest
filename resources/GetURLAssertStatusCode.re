    //Do not remove tabs. Or do remove them if you want ugly methods. Free world buddy.
    @Test
    public void %testcaseName() {

        given().
        %parameters
        when().
            %reqType(%testURL).
        then().
            assertThat().
            statusCode(%statusCode).
            %assertions
    }