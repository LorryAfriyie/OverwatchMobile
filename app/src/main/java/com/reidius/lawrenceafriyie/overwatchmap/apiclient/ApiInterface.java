package com.reidius.lawrenceafriyie.overwatchmap.apiclient;

import com.reidius.lawrenceafriyie.overwatchmap.models.Campus;
import com.reidius.lawrenceafriyie.overwatchmap.models.EmergencyService;
import com.reidius.lawrenceafriyie.overwatchmap.models.Hotspot;
import com.reidius.lawrenceafriyie.overwatchmap.models.Login;
import com.reidius.lawrenceafriyie.overwatchmap.models.Person;
import com.reidius.lawrenceafriyie.overwatchmap.models.ReportIncident;
import com.reidius.lawrenceafriyie.overwatchmap.models.Safezone;
import com.reidius.lawrenceafriyie.overwatchmap.models.Student;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("api/EMERGENCYSERVICE")
    Call<List<EmergencyService>> getEmergencyService();

    @GET("api/SAFEZONE")
    Call <List<Safezone>> getSafezones();

    @GET("api/HOTSPOT")
    Call <List<Hotspot>> getHotspots();

    @POST("api/REPORTINCIDENT")
    Call<ReportIncident> sendReport(@Body ReportIncident reportIncident);

    @PUT("api/REPORTINCIDENT")
    Call<ReportIncident> updateIncident(@Body ReportIncident reportIncident);

    @POST("api/PERSON")
    Call<Person> person(@Body Person person);

    @GET("api/PERSON/{id}")
    Call<Person> getPerson(@Path("id") Integer personID);

    @PUT("api/PERSON")
    Call<Person> updatePerson(@Body Person person);

    @POST("api/LOGIN")
    Call<Login> login(@Body Login login);

    @PUT("api/LOGIN")
    Call<Login> updateLogin(@Query("id") Integer loginID, @Query("password") String password);

    @GET("api/LOGIN")
    Call<Login> getLogin(@Query("loginID") Integer loginID, @Query("password") String password);

    @POST("api/STUDENT")
    Call<Student> student(@Body Student student);

    @GET("api/REPORTINCIDENT")
    Call<List<ReportIncident>> getIncidents(@Query("personID") Integer personID, @Query("status") String status);
    @GET("api/CAMPUS")
    Call<List<Campus>> getCampus();


}
