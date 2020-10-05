    @Test
    public void %testcaseName(%methodParameter) {

        given().
        %parameters
        when().
            %reqType(%testURL).
        then().
            assertThat().
            statusCode(%statusCode).
            %assertions
    }