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