package com.application.venturaapp.api

import com.application.venturaapp.fitosanitario.entity.*
import com.application.venturaapp.maquinarias.entity.VSAGRRMAQ
import com.application.venturaapp.home.fragment.entities.*
import com.application.venturaapp.laborCultural.entity.*
import com.application.venturaapp.login.entity.LoginResponse
import com.application.venturaapp.login.entity.UserLoginResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("VSAGRUSUA('{Code}')")
    fun UserCredentials( @Path("Code") code :String,@Header ("Cookie") session : String ): Call<UserLoginResponse>
    @GET("VSAGRUSUA")
    fun UserCredentialsMemory(@Header ("Cookie") session : String ): Call<PersonalResponse<UserLoginResponse>>

    @POST("Login")
    fun loginUser(@Body jsonObject: JsonObject): Call<LoginResponse>

    @PATCH("VSAGRUSUA('{Code}')")
    fun updateUsuario(@Header ("Cookie") session : String , @Path("Code") code :String, @Body jsonObject: JsonObject): Call<String>

    @POST("ForgetPass.xsjs/{Code}")
    fun password(@Path("Code") code :String): Call<LoginResponse>

    @GET("BusinessPartners")
    fun listaProveedor(@Header ("Cookie") session : String, @Header ("Prefer") prefer : String ): Call<PersonalResponse<Proveedor>>

    @GET("VSAGRLACA")
    fun laboresPersonal(@Header ("Cookie") session : String , @Header ("Prefer") prefer : String  ): Call<PersonalResponse<LaboresPersonal>>

    @GET("VSAGRPERS")
    fun listPersonal(@Header ("Cookie") session : String  , @Header ("Prefer") prefer : String ): Call<PersonalResponse<PersonalDato>>

    @POST("VSAGRPERS")
    fun addPersonal(@Header ("Cookie") session : String ,@Body jsonObject: JsonObject  ): Call<PersonalDato>

    @PATCH("VSAGRPERS('{Code}')")
    fun updatePersonal(@Header ("Cookie") session : String , @Path("Code") code :String,@Body jsonObject: JsonObject ) :Call<String>

    @PUT("VSAGRPERS('{Code}')")
    fun deleteContacto(@Header ("Cookie") session : String , @Path("Code") code :String,@Body jsonObject: JsonObject ) :Call<String>

    @GET("VSAGRPERS('{Code}')")
    fun  listPersonalCode(@Header ("Cookie") session : String , @Path("Code") code :String ): Call<PersonalDato>

    @GET("VSAGRCAMP")
    fun listCampanas(@Header ("Cookie") session : String  ): Call<PersonalResponse<Campania>>

    @GET("VSAGRPEPS")
    fun listPEPLaborCultural(@Header ("Cookie") session : String  ): Call<PersonalResponse<PEPDato>>

    @GET("VSAGRLCUL")
    fun listLaborCultural(@Header ("Cookie") session : String, @Header ("Prefer") prefer : String): Call<LaborCulturalPEPResponse>

    @GET("VSAGRLCUL({Code})")
    fun listDetallLaborCultural(@Header ("Cookie") session : String , @Path("Code") code :String ): Call<LaborCulturalListResponse>

    @PUT("VSAGRLCUL({Code})")
    fun actualizarlLaborCultural(@Header ("Cookie") session : String , @Path("Code") code :String , @Body jsonObject: JsonObject): Call<LaborCulturalListResponse>

    @PATCH("VSAGRLCUL({Code})")
    fun send(@Header ("Cookie") session : String , @Path("Code") code :String , @Body jsonObject: JsonObject): Call<String>

    @POST("VSAGRLCUL")
    fun addLaborCultural(@Header ("Cookie") session : String, @Body jsonObject: JsonObject): Call<LaborCulturalListResponse>

//, @Query("'$'+'filter=U_VS_AGR_CDPP eq '") filtro :String
    @GET("VSAGRPEPS('{Code}')/VS_AGR_EPEPCollection")
    fun  listEtapaProducción(@Header ("Cookie") session : String , @Path("Code", encoded = true) code :String ): Call<EtapaProduccionResponse>

    @GET("VS_AGR_PCUL")
    fun  laboresPlanificadas(@Header ("Cookie") session : String): Call<PersonalResponse<VSAGRPCULResponse>>
    /*CONTABILIZACION*/

    @GET("ProductionOrders({Code})")
    fun verificar(@Header ("Cookie") session : String, @Path("Code") code :String): Call<ContActualizacionResponse>

    @PATCH("ProductionOrders({Code})")
    fun ContabilizarPaso1(@Header ("Cookie") session : String, @Path("Code") code :String , @Body jsonObject: JsonObject): Call<String>

    @POST("InventoryGenExits")
    fun ContabilizarPaso2(@Header ("Cookie") session : String, @Body jsonObject: JsonObject): Call<SendResponse>

    /*ALMACEN*/
    @GET("Warehouses")
    fun listAlmacen(@Header ("Cookie") session : String  ): Call<GeneralResponse<Almacen>>

    @GET("Items('{Code}')/ManageBatchNumbers")
    fun ValidarLote(@Header ("Cookie") session : String , @Path("Code") code :String  ): Call<ValidarLote>

    /*FERTILIZANTES Y QUIMICOS*/

    @GET("VS_AGR_FEQU")
    fun listFertilizantesQuimicos(@Header ("Cookie") session : String  ): Call<GeneralResponse<VSAGRFEQU>>

    /*PEPS  FITOSANITARIO */

    @GET("VS_AGR_RFIT")
    fun listPEPFitosanitario(@Header ("Cookie") session : String  ): Call<GeneralResponse<VSAGRRFIT>>

    @GET("VS_AGR_RFIT({Code})")
    fun listDetalleFitosanitario(@Header ("Cookie") session : String , @Path("Code") code :String ): Call<VSAGRRFIT>

    @GET("VS_AGR_RFIT({Code})/VS_AGR_DSFTCollection")
    fun listFitosanitarioDistribucion(@Header ("Cookie") session : String , @Path("Code") code :Int ): Call<DistribucionResponse>

    /*PEPS  FERTILIZANTE */

    @GET("VS_AGR_RFER")
    fun listPEPFertilizante(@Header ("Cookie") session : String  ): Call<GeneralResponse<VSAGRRFER>>

    /*PEPS  MAQUINARIAS */

    @GET("VS_AGR_RMAQ")
    fun listPEPFMaquinaria(@Header ("Cookie") session : String  ): Call<GeneralResponse<VSAGRRMAQ>>

/*    fun getInspecciones(@Body jsonObject: JsonObject): Call<ServiceResponse<InspeccionDato>>

    @POST("ayudalista/ComboBox")
    fun getItems(@Body jsonObject: JsonObject): Call<ServiceResponse<AyudaListaDato>>

    @POST("inspeccion/ListarInspeccionPorUsuario")
    fun getInspeccionesPendientes(@Body jsonObject: JsonObject): Call<ServiceResponse<InspeccionDatos>>

    @POST("inspeccion/RegistrarInspeccionPlaneada")
    fun registrarInspeccionPlaneada(@Body jsonObject: JsonObject): Call<ServiceResponse<Int>>

    @POST("inspeccion/RegistrarInspeccionArchivo")
    fun sendFiles(@Body jsonObject: JsonObject): Call<ServiceResponse<Int>>

    @POST("inspeccion/ObtenerDatosInspeccionPersona")
    fun getDatosPersona(@Body jsonObject: JsonObject): Call<ServiceResponseTwo<Persona>>

    @POST("inspeccion/ListarInspeccionObservacion")
    fun getPersonasInspeccion(@Body jsonObject: JsonObject): Call<ServiceResponse<InspecObservacion>>

    @POST("inspeccion/ListarObservacionAsignada")
    fun getPersonasInspeccionAsignada(@Body jsonObject: JsonObject): Call<ServiceResponse<InspecObservacion>>

    @POST("inspeccion/ObtenerInspeccionObservacionDatosPorId")
    fun getObservacion(@Body jsonObject: JsonObject): Call<ServiceResponseTwo<Observacion>>

    @POST("inspeccion/ObtenerArchivosInspeccionObservacion")
    fun getObservacionArchivos(@Body jsonObject: JsonObject): Call<ServiceResponse<ArchivoObservacion>>

    @POST("inspeccion/ObtenerFirmaArchivo")
    fun fileSignature(@Body jsonObject: JsonObject): Call<ServiceResponseTwo<Signature>>

    @POST("inspeccion/RegistrarInspeccionObservacionLevantamiento")
    fun registrarLevantamientoDatos(@Body jsonObject: JsonObject): Call<ServiceResponse<String>>*/
}