
package com.nsu.midpointmassiveoperations.jira.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "issuetype",
    "timespent",
    "project",
    "fixVersions",
    "customfield_10110",
    "customfield_10111",
    "aggregatetimespent",
    "customfield_10112",
    "resolution",
    "customfield_10113",
    "customfield_10114",
    "customfield_10104",
    "customfield_10105",
    "customfield_10106",
    "customfield_10107",
    "customfield_10108",
    "customfield_10109",
    "resolutiondate",
    "workratio",
    "lastViewed",
    "watches",
    "created",
    "priority",
    "customfield_10100",
    "customfield_10101",
    "customfield_10102",
    "labels",
    "customfield_10103",
    "timeestimate",
    "aggregatetimeoriginalestimate",
    "versions",
    "issuelinks",
    "assignee",
    "updated",
    "status",
    "components",
    "timeoriginalestimate",
    "description",
    "customfield_10005",
    "aggregatetimeestimate",
    "summary",
    "creator",
    "subtasks",
    "customfield_10120",
    "reporter",
    "customfield_10121",
    "customfield_10000",
    "aggregateprogress",
    "customfield_10001",
    "customfield_10115",
    "customfield_10116",
    "environment",
    "duedate",
    "progress",
    "votes",
    "customfield_10117"
})
public class Fields {

    @JsonProperty("issuetype")
    private Issuetype issuetype;
    @JsonProperty("timespent")
    private Object timespent;
    @JsonProperty("project")
    private Project project;
    @JsonProperty("fixVersions")
    private List<FixVersion> fixVersions = null;
    @JsonProperty("customfield_10110")
    private Object customfield10110;
    @JsonProperty("customfield_10111")
    private String customfield10111;
    @JsonProperty("aggregatetimespent")
    private Object aggregatetimespent;
    @JsonProperty("customfield_10112")
    private Object customfield10112;
    @JsonProperty("resolution")
    private Resolution resolution;
    @JsonProperty("customfield_10113")
    private Object customfield10113;
    @JsonProperty("customfield_10114")
    private Object customfield10114;
    @JsonProperty("customfield_10104")
    private Object customfield10104;
    @JsonProperty("customfield_10105")
    private Object customfield10105;
    @JsonProperty("customfield_10106")
    private Object customfield10106;
    @JsonProperty("customfield_10107")
    private Object customfield10107;
    @JsonProperty("customfield_10108")
    private Object customfield10108;
    @JsonProperty("customfield_10109")
    private Object customfield10109;
    @JsonProperty("resolutiondate")
    private String resolutiondate;
    @JsonProperty("workratio")
    private Integer workratio;
    @JsonProperty("lastViewed")
    private Object lastViewed;
    @JsonProperty("watches")
    private Watches watches;
    @JsonProperty("created")
    private String created;
    @JsonProperty("priority")
    private Priority priority;
    @JsonProperty("customfield_10100")
    private Object customfield10100;
    @JsonProperty("customfield_10101")
    private String customfield10101;
    @JsonProperty("customfield_10102")
    private Object customfield10102;
    @JsonProperty("labels")
    private List<Object> labels = null;
    @JsonProperty("customfield_10103")
    private Object customfield10103;
    @JsonProperty("timeestimate")
    private Object timeestimate;
    @JsonProperty("aggregatetimeoriginalestimate")
    private Object aggregatetimeoriginalestimate;
    @JsonProperty("versions")
    private List<Object> versions = null;
    @JsonProperty("issuelinks")
    private List<Object> issuelinks = null;
    @JsonProperty("assignee")
    private Assignee assignee;
    @JsonProperty("updated")
    private String updated;
    @JsonProperty("status")
    private Status status;
    @JsonProperty("components")
    private List<Object> components = null;
    @JsonProperty("timeoriginalestimate")
    private Object timeoriginalestimate;
    @JsonProperty("description")
    private Object description;
    @JsonProperty("customfield_10005")
    private Object customfield10005;
    @JsonProperty("aggregatetimeestimate")
    private Object aggregatetimeestimate;
    @JsonProperty("summary")
    private String summary;
    @JsonProperty("creator")
    private Creator creator;
    @JsonProperty("subtasks")
    private List<Object> subtasks = null;
    @JsonProperty("customfield_10120")
    private Object customfield10120;
    @JsonProperty("reporter")
    private Reporter reporter;
    @JsonProperty("customfield_10121")
    private Object customfield10121;
    @JsonProperty("customfield_10000")
    private String customfield10000;
    @JsonProperty("aggregateprogress")
    private Aggregateprogress aggregateprogress;
    @JsonProperty("customfield_10001")
    private Object customfield10001;
    @JsonProperty("customfield_10115")
    private List<String> customfield10115 = null;
    @JsonProperty("customfield_10116")
    private String customfield10116;
    @JsonProperty("environment")
    private Object environment;
    @JsonProperty("duedate")
    private Object duedate;
    @JsonProperty("progress")
    private Progress progress;
    @JsonProperty("votes")
    private Votes votes;
    @JsonProperty("customfield_10117")
    private Integer customfield10117;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("issuetype")
    public Issuetype getIssuetype() {
        return issuetype;
    }

    @JsonProperty("issuetype")
    public void setIssuetype(Issuetype issuetype) {
        this.issuetype = issuetype;
    }

    @JsonProperty("timespent")
    public Object getTimespent() {
        return timespent;
    }

    @JsonProperty("timespent")
    public void setTimespent(Object timespent) {
        this.timespent = timespent;
    }

    @JsonProperty("project")
    public Project getProject() {
        return project;
    }

    @JsonProperty("project")
    public void setProject(Project project) {
        this.project = project;
    }

    @JsonProperty("fixVersions")
    public List<FixVersion> getFixVersions() {
        return fixVersions;
    }

    @JsonProperty("fixVersions")
    public void setFixVersions(List<FixVersion> fixVersions) {
        this.fixVersions = fixVersions;
    }

    @JsonProperty("customfield_10110")
    public Object getCustomfield10110() {
        return customfield10110;
    }

    @JsonProperty("customfield_10110")
    public void setCustomfield10110(Object customfield10110) {
        this.customfield10110 = customfield10110;
    }

    @JsonProperty("customfield_10111")
    public String getCustomfield10111() {
        return customfield10111;
    }

    @JsonProperty("customfield_10111")
    public void setCustomfield10111(String customfield10111) {
        this.customfield10111 = customfield10111;
    }

    @JsonProperty("aggregatetimespent")
    public Object getAggregatetimespent() {
        return aggregatetimespent;
    }

    @JsonProperty("aggregatetimespent")
    public void setAggregatetimespent(Object aggregatetimespent) {
        this.aggregatetimespent = aggregatetimespent;
    }

    @JsonProperty("customfield_10112")
    public Object getCustomfield10112() {
        return customfield10112;
    }

    @JsonProperty("customfield_10112")
    public void setCustomfield10112(Object customfield10112) {
        this.customfield10112 = customfield10112;
    }

    @JsonProperty("resolution")
    public Resolution getResolution() {
        return resolution;
    }

    @JsonProperty("resolution")
    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    @JsonProperty("customfield_10113")
    public Object getCustomfield10113() {
        return customfield10113;
    }

    @JsonProperty("customfield_10113")
    public void setCustomfield10113(Object customfield10113) {
        this.customfield10113 = customfield10113;
    }

    @JsonProperty("customfield_10114")
    public Object getCustomfield10114() {
        return customfield10114;
    }

    @JsonProperty("customfield_10114")
    public void setCustomfield10114(Object customfield10114) {
        this.customfield10114 = customfield10114;
    }

    @JsonProperty("customfield_10104")
    public Object getCustomfield10104() {
        return customfield10104;
    }

    @JsonProperty("customfield_10104")
    public void setCustomfield10104(Object customfield10104) {
        this.customfield10104 = customfield10104;
    }

    @JsonProperty("customfield_10105")
    public Object getCustomfield10105() {
        return customfield10105;
    }

    @JsonProperty("customfield_10105")
    public void setCustomfield10105(Object customfield10105) {
        this.customfield10105 = customfield10105;
    }

    @JsonProperty("customfield_10106")
    public Object getCustomfield10106() {
        return customfield10106;
    }

    @JsonProperty("customfield_10106")
    public void setCustomfield10106(Object customfield10106) {
        this.customfield10106 = customfield10106;
    }

    @JsonProperty("customfield_10107")
    public Object getCustomfield10107() {
        return customfield10107;
    }

    @JsonProperty("customfield_10107")
    public void setCustomfield10107(Object customfield10107) {
        this.customfield10107 = customfield10107;
    }

    @JsonProperty("customfield_10108")
    public Object getCustomfield10108() {
        return customfield10108;
    }

    @JsonProperty("customfield_10108")
    public void setCustomfield10108(Object customfield10108) {
        this.customfield10108 = customfield10108;
    }

    @JsonProperty("customfield_10109")
    public Object getCustomfield10109() {
        return customfield10109;
    }

    @JsonProperty("customfield_10109")
    public void setCustomfield10109(Object customfield10109) {
        this.customfield10109 = customfield10109;
    }

    @JsonProperty("resolutiondate")
    public String getResolutiondate() {
        return resolutiondate;
    }

    @JsonProperty("resolutiondate")
    public void setResolutiondate(String resolutiondate) {
        this.resolutiondate = resolutiondate;
    }

    @JsonProperty("workratio")
    public Integer getWorkratio() {
        return workratio;
    }

    @JsonProperty("workratio")
    public void setWorkratio(Integer workratio) {
        this.workratio = workratio;
    }

    @JsonProperty("lastViewed")
    public Object getLastViewed() {
        return lastViewed;
    }

    @JsonProperty("lastViewed")
    public void setLastViewed(Object lastViewed) {
        this.lastViewed = lastViewed;
    }

    @JsonProperty("watches")
    public Watches getWatches() {
        return watches;
    }

    @JsonProperty("watches")
    public void setWatches(Watches watches) {
        this.watches = watches;
    }

    @JsonProperty("created")
    public String getCreated() {
        return created;
    }

    @JsonProperty("created")
    public void setCreated(String created) {
        this.created = created;
    }

    @JsonProperty("priority")
    public Priority getPriority() {
        return priority;
    }

    @JsonProperty("priority")
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @JsonProperty("customfield_10100")
    public Object getCustomfield10100() {
        return customfield10100;
    }

    @JsonProperty("customfield_10100")
    public void setCustomfield10100(Object customfield10100) {
        this.customfield10100 = customfield10100;
    }

    @JsonProperty("customfield_10101")
    public String getCustomfield10101() {
        return customfield10101;
    }

    @JsonProperty("customfield_10101")
    public void setCustomfield10101(String customfield10101) {
        this.customfield10101 = customfield10101;
    }

    @JsonProperty("customfield_10102")
    public Object getCustomfield10102() {
        return customfield10102;
    }

    @JsonProperty("customfield_10102")
    public void setCustomfield10102(Object customfield10102) {
        this.customfield10102 = customfield10102;
    }

    @JsonProperty("labels")
    public List<Object> getLabels() {
        return labels;
    }

    @JsonProperty("labels")
    public void setLabels(List<Object> labels) {
        this.labels = labels;
    }

    @JsonProperty("customfield_10103")
    public Object getCustomfield10103() {
        return customfield10103;
    }

    @JsonProperty("customfield_10103")
    public void setCustomfield10103(Object customfield10103) {
        this.customfield10103 = customfield10103;
    }

    @JsonProperty("timeestimate")
    public Object getTimeestimate() {
        return timeestimate;
    }

    @JsonProperty("timeestimate")
    public void setTimeestimate(Object timeestimate) {
        this.timeestimate = timeestimate;
    }

    @JsonProperty("aggregatetimeoriginalestimate")
    public Object getAggregatetimeoriginalestimate() {
        return aggregatetimeoriginalestimate;
    }

    @JsonProperty("aggregatetimeoriginalestimate")
    public void setAggregatetimeoriginalestimate(Object aggregatetimeoriginalestimate) {
        this.aggregatetimeoriginalestimate = aggregatetimeoriginalestimate;
    }

    @JsonProperty("versions")
    public List<Object> getVersions() {
        return versions;
    }

    @JsonProperty("versions")
    public void setVersions(List<Object> versions) {
        this.versions = versions;
    }

    @JsonProperty("issuelinks")
    public List<Object> getIssuelinks() {
        return issuelinks;
    }

    @JsonProperty("issuelinks")
    public void setIssuelinks(List<Object> issuelinks) {
        this.issuelinks = issuelinks;
    }

    @JsonProperty("assignee")
    public Assignee getAssignee() {
        return assignee;
    }

    @JsonProperty("assignee")
    public void setAssignee(Assignee assignee) {
        this.assignee = assignee;
    }

    @JsonProperty("updated")
    public String getUpdated() {
        return updated;
    }

    @JsonProperty("updated")
    public void setUpdated(String updated) {
        this.updated = updated;
    }

    @JsonProperty("status")
    public Status getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Status status) {
        this.status = status;
    }

    @JsonProperty("components")
    public List<Object> getComponents() {
        return components;
    }

    @JsonProperty("components")
    public void setComponents(List<Object> components) {
        this.components = components;
    }

    @JsonProperty("timeoriginalestimate")
    public Object getTimeoriginalestimate() {
        return timeoriginalestimate;
    }

    @JsonProperty("timeoriginalestimate")
    public void setTimeoriginalestimate(Object timeoriginalestimate) {
        this.timeoriginalestimate = timeoriginalestimate;
    }

    @JsonProperty("description")
    public Object getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(Object description) {
        this.description = description;
    }

    @JsonProperty("customfield_10005")
    public Object getCustomfield10005() {
        return customfield10005;
    }

    @JsonProperty("customfield_10005")
    public void setCustomfield10005(Object customfield10005) {
        this.customfield10005 = customfield10005;
    }

    @JsonProperty("aggregatetimeestimate")
    public Object getAggregatetimeestimate() {
        return aggregatetimeestimate;
    }

    @JsonProperty("aggregatetimeestimate")
    public void setAggregatetimeestimate(Object aggregatetimeestimate) {
        this.aggregatetimeestimate = aggregatetimeestimate;
    }

    @JsonProperty("summary")
    public String getSummary() {
        return summary;
    }

    @JsonProperty("summary")
    public void setSummary(String summary) {
        this.summary = summary;
    }

    @JsonProperty("creator")
    public Creator getCreator() {
        return creator;
    }

    @JsonProperty("creator")
    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    @JsonProperty("subtasks")
    public List<Object> getSubtasks() {
        return subtasks;
    }

    @JsonProperty("subtasks")
    public void setSubtasks(List<Object> subtasks) {
        this.subtasks = subtasks;
    }

    @JsonProperty("customfield_10120")
    public Object getCustomfield10120() {
        return customfield10120;
    }

    @JsonProperty("customfield_10120")
    public void setCustomfield10120(Object customfield10120) {
        this.customfield10120 = customfield10120;
    }

    @JsonProperty("reporter")
    public Reporter getReporter() {
        return reporter;
    }

    @JsonProperty("reporter")
    public void setReporter(Reporter reporter) {
        this.reporter = reporter;
    }

    @JsonProperty("customfield_10121")
    public Object getCustomfield10121() {
        return customfield10121;
    }

    @JsonProperty("customfield_10121")
    public void setCustomfield10121(Object customfield10121) {
        this.customfield10121 = customfield10121;
    }

    @JsonProperty("customfield_10000")
    public String getCustomfield10000() {
        return customfield10000;
    }

    @JsonProperty("customfield_10000")
    public void setCustomfield10000(String customfield10000) {
        this.customfield10000 = customfield10000;
    }

    @JsonProperty("aggregateprogress")
    public Aggregateprogress getAggregateprogress() {
        return aggregateprogress;
    }

    @JsonProperty("aggregateprogress")
    public void setAggregateprogress(Aggregateprogress aggregateprogress) {
        this.aggregateprogress = aggregateprogress;
    }

    @JsonProperty("customfield_10001")
    public Object getCustomfield10001() {
        return customfield10001;
    }

    @JsonProperty("customfield_10001")
    public void setCustomfield10001(Object customfield10001) {
        this.customfield10001 = customfield10001;
    }

    @JsonProperty("customfield_10115")
    public List<String> getCustomfield10115() {
        return customfield10115;
    }

    @JsonProperty("customfield_10115")
    public void setCustomfield10115(List<String> customfield10115) {
        this.customfield10115 = customfield10115;
    }

    @JsonProperty("customfield_10116")
    public String getCustomfield10116() {
        return customfield10116;
    }

    @JsonProperty("customfield_10116")
    public void setCustomfield10116(String customfield10116) {
        this.customfield10116 = customfield10116;
    }

    @JsonProperty("environment")
    public Object getEnvironment() {
        return environment;
    }

    @JsonProperty("environment")
    public void setEnvironment(Object environment) {
        this.environment = environment;
    }

    @JsonProperty("duedate")
    public Object getDuedate() {
        return duedate;
    }

    @JsonProperty("duedate")
    public void setDuedate(Object duedate) {
        this.duedate = duedate;
    }

    @JsonProperty("progress")
    public Progress getProgress() {
        return progress;
    }

    @JsonProperty("progress")
    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    @JsonProperty("votes")
    public Votes getVotes() {
        return votes;
    }

    @JsonProperty("votes")
    public void setVotes(Votes votes) {
        this.votes = votes;
    }

    @JsonProperty("customfield_10117")
    public Integer getCustomfield10117() {
        return customfield10117;
    }

    @JsonProperty("customfield_10117")
    public void setCustomfield10117(Integer customfield10117) {
        this.customfield10117 = customfield10117;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
