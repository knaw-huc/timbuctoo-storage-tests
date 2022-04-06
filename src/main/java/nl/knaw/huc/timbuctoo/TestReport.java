package nl.knaw.huc.timbuctoo;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static nl.knaw.huc.timbuctoo.TestCase.*;

public class TestReport {
    private final List<TestCase> testCases;

    public TestReport(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    public String getReports() {
        return testCases.stream().map(testCase -> testCase.getName() + "\n" +
                "#".repeat(70) + "\n" +
                getReportLine(IMPORT, testCase.importAll) +
                getReportLine(GET_ALL_LATEST, testCase.getAllLatest) +
                getReportLine(GET_ALL_FROM_TYPE_LATEST, testCase.getAllFromTypeLatest) +
                getReportLine(GET_ALL_ABOUT_SUBJECT_LATEST, testCase.getAllAboutSubjectLatest) +
                getReportLine(GET_SUBJECTS_VERSION, testCase.getSubjectsOfVersion) +
                getReportLine(GET_ADDED_CHANGES_VERSION, testCase.getAddedChangesOfVersion) +
                getReportLine(GET_DELETED_CHANGES_VERSION, testCase.getDeletedChangesOfVersion) +
                "#".repeat(70) + "\n"
        ).collect(Collectors.joining("\n"));
    }

    public String getFullReport() {
        return " ".repeat(50) +
                String.format("%-" + 15 * testCases.size() + "s", "Count") +
                String.format("%-" + 15 * testCases.size() + "s", "Time") + "\n" +
                " ".repeat(50) +
                withCases(testCases, tc -> String.format("%15s", tc.getName())) +
                withCases(testCases, tc -> String.format("%15s", tc.getName())) + "\n" +
                "#".repeat(50) +
                withCases(testCases, tc -> "#".repeat(30)) + "\n" +
                getFullReportLine(IMPORT, tc -> tc.importAll) +
                getFullReportLine(GET_ALL_LATEST, tc -> tc.getAllLatest) +
                getFullReportLine(GET_ALL_FROM_TYPE_LATEST, tc -> tc.getAllFromTypeLatest) +
                getFullReportLine(GET_ALL_ABOUT_SUBJECT_LATEST, tc -> tc.getAllAboutSubjectLatest) +
                getFullReportLine(GET_SUBJECTS_VERSION, tc -> tc.getSubjectsOfVersion) +
                getFullReportLine(GET_ADDED_CHANGES_VERSION, tc -> tc.getAddedChangesOfVersion) +
                getFullReportLine(GET_DELETED_CHANGES_VERSION, tc -> tc.getDeletedChangesOfVersion) +
                "#".repeat(50) +
                withCases(testCases, tc -> "#".repeat(30)) + "\n";
    }

    private static String getReportLine(String name, TestResult result) {
        return String.format("%-50s%-10s%10s", name, result != null ? result.count : "-", result != null ? result.stopwatch : "-") + "\n";
    }

    private String getFullReportLine(String name, Function<TestCase, TestResult> withResult) {
        List<TestResult> testResults = testCases.stream().map(withResult).collect(Collectors.toList());
        return String.format("%-50s", name) +
                withCases(testResults, result -> String.format("%15s", result != null ? String.valueOf(result.count) : "-")) +
                withCases(testResults, result -> String.format("%15s", result != null ? String.valueOf(result.stopwatch) : "-")) +
                "\n";
    }

    private static <R> String withCases(List<R> testCases, Function<R, String> toText) {
        return testCases.stream().map(toText).collect(Collectors.joining(""));
    }
}
