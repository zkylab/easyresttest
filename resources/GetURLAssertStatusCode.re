@Test
public void %testcaseName() {

    given().
    %parameters
    when().
        get(%testURL).
    then().
        assertThat().
        statusCode(%statusCode).
}