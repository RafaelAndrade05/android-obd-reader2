package com.sohrab.obd.reader.trip;


import android.content.Context;

import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.constants.DefineObdReader;
import com.sohrab.obd.reader.enums.AvailableCommandNames;
import com.sohrab.obd.reader.enums.FuelType;
import com.sohrab.obd.reader.obdCommand.ObdCommand;
import com.sohrab.obd.reader.obdCommand.SpeedCommand;
import com.sohrab.obd.reader.obdCommand.engine.MassAirFlowCommand;
import com.sohrab.obd.reader.obdCommand.engine.RPMCommand;
import com.sohrab.obd.reader.obdCommand.engine.RuntimeCommand;
import com.sohrab.obd.reader.obdCommand.fuel.FindFuelTypeCommand;
import com.sohrab.obd.reader.obdCommand.pressure.IntakeManifoldPressureCommand;
import com.sohrab.obd.reader.obdCommand.temperature.AirIntakeTemperatureCommand;

import java.util.ArrayList;
import java.util.UUID;

import app.com.android_obd_reader.R;


/**
 * Created by sohrab on 30/11/2017.
 */

public class TripRecord implements DefineObdReader {
    private static Context sContext;
    int MINUS_ONE = -1;
    private static final int SPEED_GAP = 20;
    private static final float GRAM_TO_LITRE_GASOLIN = 748.9f;
    private static final float GRAM_TO_LITRE_DIESEL = 850.8f;
    private static final float GRAM_TO_LITRE_CNG = 128.2f;
    private static final float GRAM_TO_LITRE_METHANOL = 786.6f;
    private static final float GRAM_TO_LITRE_ETHANOL = 789f;
    private static final float GRAM_TO_LITRE_PROPANE = 493f;
    private final static String ENGINE_RPM = "Engine RPM/RPM del Motor";
    private final static String VEHICLE_SPEED = "Vehicle Speed/Velocidad del vehiculo";
    private final static String ENGINE_RUNTIME = "Engine Runtime/Motor runtime";
    private final static String MAF = "Mass Air Flow/Flujo de aire masa";
    private final static String FUEL_LEVEL = "Fuel Level/Nivel de combustible";
    private final static String FUEL_TYPE = "Fuel Type/Tipo de combustible";
    private final static String INTAKE_MANIFOLD_PRESSURE = "Intake Manifold Pressure/Presion del colector de admision";
    private final static String AIR_INTAKE_TEMPERATURE = "Air Intake Temperature/Temperatura de admision de aire";
    private final static String TROUBLE_CODES = "Trouble Codes/Codigos de problema";
    private final static String AMBIENT_AIR_TEMP = "Ambient Air Temperature/Temperatura del aire ambiente";
    private final static String ENGINE_COOLANT_TEMP = "Engine Coolant Temperatura/Temperatura del refrigerante del motor";
    private final static String BAROMETRIC_PRESSURE = "Barometric Pressure/Presion barometrica";
    private final static String FUEL_PRESSURE = "Fuel Pressure/Presion de combustible";
    private final static String ENGINE_LOAD = "Engine Load/Carga del motor";
    private final static String THROTTLE_POS = "Throttle Position/Posicion del acelerador";
    private final static String FUEL_CONSUMPTION_RATE = "Fuel Consumption Rate/Tasa de consumo de combustible";
    private final static String TIMING_ADVANCE = "Timing Advance/Avance de tiempo";
    private final static String PERMANENT_TROUBLE_CODES = "Permanent Trouble Codes/Codigos de problemas permanentes";
    private final static String PENDING_TROUBLE_CODES = "Pending Trouble Codes/Codigos de problemas pendientes";
    private final static String EQUIV_RATIO = "Command Equivalence Ratio/Relacion de equivalencia de comando";
    private final static String DISTANCE_TRAVELED_AFTER_CODES_CLEARED = "Distance since codes cleared/Distancia desde que se borraron los codigos";
    private final static String CONTROL_MODULE_VOLTAGE = "Control Module Power Supply/Fuente de alimentacion del moduolo de control";
    private final static String ENGINE_FUEL_RATE = "Engine Fuel Rate/Tasa de combustible del motor";
    private final static String FUEL_RAIL_PRESSURE = "Fuel Rail Pressure/Presion del riel de combustible";
    private final static String VIN = "Vehicle Identification Number (VIN)/Numero de ID del vehiculo";
    private final static String DISTANCE_TRAVELED_MIL_ON = "Distance traveled with MIL on/Distancia recorrida con MILL en";
    private final static String DTC_NUMBER = "Diagnostic Trouble Codes/Codigos de diagnostico de problemas";
    private final static String TIME_SINCE_TC_CLEARED = "Time since trouble codes cleared";
    private final static String REL_THROTTLE_POS = "Relative throttle position";
    private final static String ABS_LOAD = "Absolute load";
    private final static String ENGINE_OIL_TEMP = "Engine oil temperature/Temperatura del aceite del motor";
    private final static String AIR_FUEL_RATIO = "Air/Fuel Ratio";
    private final static String WIDEBAND_AIR_FUEL_RATIO = "Wideband Air/Fuel Ratio";
    private final static String DESCRIBE_PROTOCOL = "Describe protocol";
    private final static String DESCRIBE_PROTOCOL_NUMBER = "Describe protocol number";
    private final static String IGNITION_MONITOR = "Ignition monitor";

    private Integer engineRpmMax = 0;
    private String engineRpm;
    private Integer speed = -1;
    private Integer speedMax = 0;
    private String engineRuntime;
    private static TripRecord sInstance;
    private long tripStartTime;
    private float idlingDuration;
    private float drivingDuration;
    private long mAddSpeed;
    private long mSpeedCount;
    private float mDistanceTravel;
    private int mRapidAccTimes;
    private int mRapidDeclTimes;
    private float mInsFuelConsumption = 0.0f;
    private float mDrivingFuelConsumption = 0.0f;
    private float mIdlingFuelConsumption = 0.0f;
    private String mFuelLevel;
    private long mLastTimeStamp;
    private float mFuelTypeValue = 14.7f; // default is Gasoline fuel ratio
    private float mDrivingMaf;
    private int mDrivingMafCount;
    private float mIdleMaf;
    private int mIdleMafCount;
    private int mSecondAgoSpeed;
    private float gramToLitre = GRAM_TO_LITRE_GASOLIN;
    private String mTripIdentifier;
    private boolean mIsMAFSupported = true;
    private boolean mIsEngineRuntimeSupported = true;
    private boolean mIsTempPressureSupported = true;
    private float mIntakeAirTemp = 0.0f;
    private float mIntakePressure = 0.0f;
    private String mFaultCodes = "";
    private String mAmbientAirTemp;
    private String mEngineCoolantTemp;
    private String mEngineOilTemp;
    private String mFuelConsumptionRate;
    private String mEngineFuelRate;
    private String mFuelPressure;
    private String mEngineLoad;
    private String mBarometricPressure;
    private String mThrottlePos;
    private String mTimingAdvance;
    private String mPermanentTroubleCode;
    private String mPendingTroubleCode;
    private String mEquivRatio;
    private String mDistanceTraveledAfterCodesCleared;
    private String mControlModuleVoltage;
    private String mFuelRailPressure;
    private String mVehicleIdentificationNumber;
    private String mDistanceTraveledMilOn;
    private String mDtcNumber;
    private String mTimeSinceTcClear;
    private String mRelThottlePos;
    private String mAbsLoad;
    private String mAirFuelRatio;
    private String mWideBandAirFuelRatio;
    private String mDescribeProtocol;
    private String mDescribeProtocolNumber;
    private String mIgnitionMonitor;


    private TripRecord() {
        tripStartTime = System.currentTimeMillis();
        mTripIdentifier = UUID.randomUUID().toString();
    }

    public static TripRecord getTripRecode(Context context) {
        sContext = context;
        if (sInstance == null)
            sInstance = new TripRecord();
        return sInstance;
    }

    public void clear() {
        sInstance = null;
    }

    public void setSpeed(Integer currentSpeed) {
        calculateIdlingAndDrivingTime(currentSpeed);
        findRapidAccAndDeclTimes(currentSpeed);
        speed = currentSpeed;
        if (speedMax < currentSpeed)
            speedMax = currentSpeed;

        // find travelled distance
        if (speed != 0) {
            mAddSpeed += speed;
            mSpeedCount++;

            mDistanceTravel = (mAddSpeed / mSpeedCount * (drivingDuration / (60 * 60 * 1000)));
        }

    }

    private void findRapidAccAndDeclTimes(Integer currentSpeed) {
        if (speed == -1)
            return;

        if (System.currentTimeMillis() - mLastTimeStamp > 1000) {

            int speedDiff = currentSpeed - mSecondAgoSpeed;
            boolean acceleration = speedDiff > 0;

            if (Math.abs(speedDiff) > SPEED_GAP) {

                if (acceleration)
                    mRapidAccTimes++;
                else
                    mRapidDeclTimes++;
            }

            mSecondAgoSpeed = currentSpeed;
            mLastTimeStamp = System.currentTimeMillis();
        }
    }

    private void calculateIdlingAndDrivingTime(int currentSpeed) {

        long currentTime = System.currentTimeMillis();
        if ((speed == -1 || speed == 0) && currentSpeed == 0) {
            idlingDuration = currentTime - tripStartTime - drivingDuration;
        }
        drivingDuration = currentTime - tripStartTime - idlingDuration;
    }

    private void calculateMaf() {

        if (mIntakePressure > 0 && mIntakeAirTemp > 0) {
            float rpm = Float.parseFloat(engineRpm);
            float imap = ((rpm * mIntakePressure) / mIntakeAirTemp) / 2;
            //   float engineDisp = ObdReaderApplication.getInstance().getLoggedInUser().getDisp();
            float engineDisp = 2;
            float maf = (imap / 60.0f) * (85.0f / 100.0f) * (engineDisp) * ((28.97f) / (8.314f));
            findInsFualConsumption(maf);
        }
    }


    public Integer getSpeed() {
        if (speed == -1)
            return 0;

        return speed;
    }


    public Integer getEngineRpmMax() {
        return this.engineRpmMax;
    }

    public float getDrivingDuration() {
        return drivingDuration / 60000; //time in minutes
    }

    public float getIdlingDuration() {
        return (idlingDuration / 60000); // time in minutes
    }

    public Integer getSpeedMax() {
        return speedMax;
    }

    public float getmDistanceTravel() {
        return mDistanceTravel;
    }

    public int getmRapidAccTimes() {
        return mRapidAccTimes;
    }

    public int getmRapidDeclTimes() {
        return mRapidDeclTimes;
    }

    public String getEngineRuntime() {
        return engineRuntime;
    }

    public String getmTripIdentifier() {
        return mTripIdentifier;
    }

    public float getmGasCost() {
        return (mIsMAFSupported || mIsTempPressureSupported) ? (mIdlingFuelConsumption + mDrivingFuelConsumption) * ObdPreferences.get(sContext.getApplicationContext()).getGasPrice() : MINUS_ONE;
    }

    public boolean ismIsMAFSupported() {
        return mIsMAFSupported;
    }

    public void setmIsMAFSupported(boolean mIsMAFSupported) {
        this.mIsMAFSupported = mIsMAFSupported;
    }

    public boolean ismIsEngineRuntimeSupported() {
        return mIsEngineRuntimeSupported;
    }

    public void setmIsEngineRuntimeSupported(boolean mIsEngineRuntimeSupported) {
        this.mIsEngineRuntimeSupported = mIsEngineRuntimeSupported;
    }

    public void findInsFualConsumption(float massAirFlow) {
        if (speed > 0)
            mInsFuelConsumption = 100 * (massAirFlow / (mFuelTypeValue * gramToLitre) * 3600) / speed; // in  litre/100km
        findIdleAndDrivingFuelConsumtion(massAirFlow);
    }

    public float getmInsFuelConsumption() {
        return (mIsMAFSupported || mIsTempPressureSupported) ? mInsFuelConsumption : MINUS_ONE;
    }

    public void setEngineRpm(String value) {
        engineRpm = value;
        if (value != null && this.engineRpmMax < Integer.parseInt(value)) {
            this.engineRpmMax = Integer.parseInt(value);
        }
    }

    public void findIdleAndDrivingFuelConsumtion(float currentMaf) {

        float literPerSecond = 0;
        if (speed > 0) {
            mDrivingMaf += currentMaf;
            mDrivingMafCount++;
            literPerSecond = ((((mDrivingMaf / mDrivingMafCount) / mFuelTypeValue) / gramToLitre));
            mDrivingFuelConsumption = (literPerSecond * (drivingDuration / 1000));

        } else {
            mIdleMaf += currentMaf;
            mIdleMafCount++;
            literPerSecond = ((((mIdleMaf / mIdleMafCount) / mFuelTypeValue) / gramToLitre));
            mIdlingFuelConsumption = (literPerSecond * (idlingDuration / 1000));
        }
    }


    public float getmDrivingFuelConsumption() {
        return (mIsMAFSupported || mIsTempPressureSupported) ? mDrivingFuelConsumption : MINUS_ONE;
    }

    public float getmIdlingFuelConsumption() {
        return (mIsMAFSupported || mIsTempPressureSupported) ? mIdlingFuelConsumption : MINUS_ONE;
    }

    public String getEngineRpm() {
        return engineRpm;
    }

    public String getmFaultCodes() {
        return mFaultCodes;
    }

    public void updateTrip(String name, ObdCommand command) {

        switch (name) {

            case VEHICLE_SPEED:
                setSpeed(((SpeedCommand) command).getMetricSpeed());
                break;

            case ENGINE_RPM:
                setEngineRpm(command.getCalculatedResult());
                setmIsEngineRuntimeSupported(true);
                break;

            case MAF:
                findInsFualConsumption(Float.parseFloat(command.getFormattedResult()));
                setmIsMAFSupported(true);

                break;

            case ENGINE_RUNTIME:
                engineRuntime = command.getFormattedResult();
                break;

            case FUEL_LEVEL:
                mFuelLevel = command.getFormattedResult();
                break;

            case FUEL_TYPE:
                if (ObdPreferences.get(sContext.getApplicationContext()).getFuelType() == 0)
                    getFuelTypeValue(command.getFormattedResult());
                break;

            case INTAKE_MANIFOLD_PRESSURE:
                mIntakePressure = Float.parseFloat(command.getCalculatedResult());
                calculateMaf();
                break;

            case AIR_INTAKE_TEMPERATURE:
                mIntakeAirTemp = Float.parseFloat(command.getCalculatedResult()) + 273.15f;
                calculateMaf();
                break;

            case TROUBLE_CODES:
                mFaultCodes = command.getFormattedResult();
                break;

            case AMBIENT_AIR_TEMP:
                mAmbientAirTemp = command.getFormattedResult();
                break;

            case ENGINE_COOLANT_TEMP:
                mEngineCoolantTemp = command.getFormattedResult();
                break;

            case ENGINE_OIL_TEMP:
                mEngineOilTemp = command.getFormattedResult();
                break;

            case FUEL_CONSUMPTION_RATE:
                mFuelConsumptionRate = command.getFormattedResult();
                break;

            case ENGINE_FUEL_RATE:
                mEngineFuelRate = command.getFormattedResult();
                break;

            case FUEL_PRESSURE:
                mFuelPressure = command.getFormattedResult();
                break;


            case ENGINE_LOAD:
                mEngineLoad = command.getFormattedResult();
                break;

            case BAROMETRIC_PRESSURE:
                mBarometricPressure = command.getFormattedResult();
                break;

            case THROTTLE_POS:
                mThrottlePos = command.getFormattedResult();
                break;

            case TIMING_ADVANCE:
                mTimingAdvance = command.getFormattedResult();
                break;

            case PERMANENT_TROUBLE_CODES:
                mPermanentTroubleCode = command.getFormattedResult();
                break;

            case PENDING_TROUBLE_CODES:
                mPendingTroubleCode = command.getFormattedResult();
                break;

            case EQUIV_RATIO:
                mEquivRatio = command.getFormattedResult();
                break;

            case DISTANCE_TRAVELED_AFTER_CODES_CLEARED:
                mDistanceTraveledAfterCodesCleared = command.getFormattedResult();
                break;

            case CONTROL_MODULE_VOLTAGE:
                mControlModuleVoltage = command.getFormattedResult();
                break;

            case FUEL_RAIL_PRESSURE:
                mFuelRailPressure = command.getFormattedResult();
                break;

            case VIN:
                mVehicleIdentificationNumber = command.getFormattedResult();
                break;

            case DISTANCE_TRAVELED_MIL_ON:
                mDistanceTraveledMilOn = command.getFormattedResult();
                break;

            case DTC_NUMBER:
                mDtcNumber = command.getFormattedResult();
                break;

            case TIME_SINCE_TC_CLEARED:
                mTimeSinceTcClear = command.getFormattedResult();
                break;

            case REL_THROTTLE_POS:
                mRelThottlePos = command.getFormattedResult();
                break;

            case ABS_LOAD:
                mAbsLoad = command.getFormattedResult();
                break;

            case AIR_FUEL_RATIO:
                mAirFuelRatio = command.getFormattedResult();
                break;

            case WIDEBAND_AIR_FUEL_RATIO:
                mWideBandAirFuelRatio = command.getFormattedResult();
                break;

            case DESCRIBE_PROTOCOL:
                mDescribeProtocol = command.getFormattedResult();
                break;

            case DESCRIBE_PROTOCOL_NUMBER:
                mDescribeProtocolNumber = command.getFormattedResult();
                break;

            case IGNITION_MONITOR:
                mIgnitionMonitor = command.getFormattedResult();
                break;


        }

    }

    private void getFuelTypeValue(String fuelType) {
        float fuelTypeValue = 0;
        if (FuelType.GASOLINE.getDescription().equals(fuelType)) {
            fuelTypeValue = 14.7f;
            gramToLitre = GRAM_TO_LITRE_GASOLIN;
        } else if (FuelType.PROPANE.getDescription().equals(fuelType)) {
            fuelTypeValue = 15.5f;
            gramToLitre = GRAM_TO_LITRE_PROPANE;
        } else if (FuelType.ETHANOL.getDescription().equals(fuelType)) {
            fuelTypeValue = 9f;
            gramToLitre = GRAM_TO_LITRE_ETHANOL;
        } else if (FuelType.METHANOL.getDescription().equals(fuelType)) {
            fuelTypeValue = 6.4f;
            gramToLitre = GRAM_TO_LITRE_METHANOL;
        } else if (FuelType.DIESEL.getDescription().equals(fuelType)) {
            fuelTypeValue = 14.6f;
            gramToLitre = GRAM_TO_LITRE_DIESEL;
        } else if (FuelType.CNG.getDescription().equals(fuelType)) {
            fuelTypeValue = 17.2f;
            gramToLitre = GRAM_TO_LITRE_CNG;
        }

        if (fuelTypeValue != 0) {
            ObdPreferences.get(sContext.getApplicationContext()).setFuelType(mFuelTypeValue);
            mFuelTypeValue = fuelTypeValue;
        }
    }

    public void setmIsTempPressureSupported(boolean mIsTempPressureSupported) {
        this.mIsTempPressureSupported = mIsTempPressureSupported;
    }

    public boolean ismIsTempPressureSupported() {
        return mIsTempPressureSupported;
    }

    public String getmAmbientAirTemp() {
        return mAmbientAirTemp;
    }

    public String getmEngineCoolantTemp() {
        return mEngineCoolantTemp;
    }

    public String getmEngineOilTemp() {
        return mEngineOilTemp;
    }

    public String getmFuelConsumptionRate() {
        return mFuelConsumptionRate;
    }

    public String getmEngineFuelRate() {
        return mEngineFuelRate;
    }

    public String getmFuelPressure() {
        return mFuelPressure;
    }

    public String getmEngineLoad() {
        return mEngineLoad;
    }

    public String getmBarometricPressure() {
        return mBarometricPressure;
    }

    public String getmThrottlePos() {
        return mThrottlePos;
    }

    public String getmTimingAdvance() {
        return mTimingAdvance;
    }

    public String getmPermanentTroubleCode() {
        return mPermanentTroubleCode;
    }

    public String getmPendingTroubleCode() {
        return mPendingTroubleCode;
    }

    public String getmEquivRatio() {
        return mEquivRatio;
    }

    public String getmDistanceTraveledAfterCodesCleared() {
        return mDistanceTraveledAfterCodesCleared;
    }

    public String getmControlModuleVoltage() {
        return mControlModuleVoltage;
    }

    public String getmFuelRailPressure() {
        return mFuelRailPressure;
    }

    public String getmVehicleIdentificationNumber() {
        return mVehicleIdentificationNumber;
    }

    public String getmDistanceTraveledMilOn() {
        return mDistanceTraveledMilOn;
    }

    public String getmDtcNumber() {
        return mDtcNumber;
    }

    public String getmTimeSinceTcClear() {
        return mTimeSinceTcClear;
    }

    public String getmRelThottlePos() {
        return mRelThottlePos;
    }

    public String getmAbsLoad() {
        return mAbsLoad;
    }

    public String getmAirFuelRatio() {
        return mAirFuelRatio;
    }

    public String getmWideBandAirFuelRatio() {
        return mWideBandAirFuelRatio;
    }

    public String getmDescribeProtocol() {
        return mDescribeProtocol;
    }

    public String getmDescribeProtocolNumber() {
        return mDescribeProtocolNumber;
    }

    public String getmIgnitionMonitor() {
        return mIgnitionMonitor;
    }

   private ArrayList<ObdCommand> mObdCommandArrayList;

    public ArrayList<ObdCommand> getmObdCommandArrayList() {

        if (mObdCommandArrayList == null) {

            mObdCommandArrayList = new ArrayList<>();
            mObdCommandArrayList.add(new SpeedCommand());
            mObdCommandArrayList.add(new RPMCommand());

            if (ismIsMAFSupported()) {
                mObdCommandArrayList.add(new MassAirFlowCommand());
            } else if (ismIsTempPressureSupported()) {
                mObdCommandArrayList.add(new IntakeManifoldPressureCommand());
                mObdCommandArrayList.add(new AirIntakeTemperatureCommand());
            }

            if (ismIsEngineRuntimeSupported()) {
                mObdCommandArrayList.add(new RuntimeCommand());
            }
            mObdCommandArrayList.add(new FindFuelTypeCommand());
        }
        return mObdCommandArrayList;
    }

    public void addObdCommand(ObdCommand obdCommand) {
        if (mObdCommandArrayList == null) {
            mObdCommandArrayList = new ArrayList<>();
        }
        mObdCommandArrayList.add(obdCommand);
    }

    @Override
    public String toString() {
        return "OBD DATA/DATOS OBD:" +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.SPEED.getValue() + ":  " +
                "\n" + " " +
                "\n" + speed + " km/h" +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.ENGINE_RPM.getValue() + ": " +
                "\n" + " " +
                "\n" + engineRpm +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.ENGINE_RUNTIME.getValue() + ":  " +
                "\n" + " " +
                "\n" +  engineRuntime + "hh:mm:ss" +
                "\n" + "__________________________________________" +
                "\n" +  mFaultCodes +
                "\n" + " " +
                "\nIdling Fuel Consumtion:" +
                "\n" + " " +
                "\n" +  getmIdlingFuelConsumption() + "Litre" +
                "\n" + "__________________________________________" +
                "\nDriving Fuel Consumtion:" +
                "\n" + " " +
                "\n" + getmDrivingFuelConsumption() + " Litre" +
                "\n" + "__________________________________________" +
                "\nInstant Fuel Consumtion:" +
                "\n" + " " +
                "\n" +  mInsFuelConsumption + " L/100km" +
                "\n" + "__________________________________________" +
                "\ndriving maf: " +
                "\n" + " " +
                "\n" + mDrivingMaf + " g/s" +
                "\n" + "__________________________________________" +
                "\nidle maf: " +
                "\n" + " " +
                "\n" +  mIdleMaf + " g/s" +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.FUEL_TYPE.getValue() + ":  " +
                "\n" + " " +
                "\n"  + mFuelTypeValue +
                "\n" + "__________________________________________" +
                "\nRapid Acceleration Times: " +
                "\n" + " " +
                "\n" +  mRapidAccTimes  +
                "\n" + "__________________________________________" +
                "\nRapid Decleration Times: " +
                "\n" +   mRapidDeclTimes +
                "\n" + "__________________________________________" +
                "\nMax Rpm: " +
                "\n" + engineRpmMax +
                "\n" + "__________________________________________" +
                "\nMax Speed: " +
                "\n" + speedMax + " km/h" +
                "\n" + "__________________________________________" +
                "\nDriving Duration: " +
                "\n" + getDrivingDuration() + " minute" +
                "\n" + "__________________________________________" +
                "\nIdle Duration: " +
                "\n" + getIdlingDuration() + " minute" +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.DISTANCE_TRAVELED_AFTER_CODES_CLEARED.getValue() + ":  " +
                "\n"  + getmDistanceTraveledAfterCodesCleared() +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.DISTANCE_TRAVELED_MIL_ON.getValue() + ":  " +
                "\n"   + mDistanceTraveledMilOn +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.INTAKE_MANIFOLD_PRESSURE.getValue() + ":  " +
                "\n" + mIntakePressure + " kpa" +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.AIR_INTAKE_TEMP.getValue() + ":  " +
                "\n" + mIntakeAirTemp + " C" +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.FUEL_CONSUMPTION_RATE.getValue() + ":  " +
                "\n" + mFuelConsumptionRate + " L/h" +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.FUEL_LEVEL.getValue() + ":  " +
                "\n" + mFuelLevel +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.FUEL_PRESSURE.getValue() + ":  " +
                "\n" + mFuelPressure +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.ENGINE_FUEL_RATE.getValue() + ":  " +
                "\n" + mEngineFuelRate +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.ENGINE_COOLANT_TEMP.getValue() + ":  " +
                "\n" + mEngineCoolantTemp +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.ENGINE_LOAD.getValue() + ":  " +
                "\n" + mEngineLoad +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.ENGINE_OIL_TEMP.getValue() + ":  " +
                "\n" + mEngineOilTemp +
                "\n" + "__________________________________________" +

                "\n" + AvailableCommandNames.BAROMETRIC_PRESSURE.getValue() + ":  " + mBarometricPressure +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.AIR_FUEL_RATIO.getValue() + ":  " + mAirFuelRatio +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.WIDEBAND_AIR_FUEL_RATIO.getValue() + ":  " + mWideBandAirFuelRatio +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.ABS_LOAD.getValue() + ":  " + mAbsLoad +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.CONTROL_MODULE_VOLTAGE.getValue() + ":  " + mControlModuleVoltage +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.EQUIV_RATIO.getValue() + ":  " + mEquivRatio +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.DTC_NUMBER.getValue() + ":  " + mDtcNumber +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.DESCRIBE_PROTOCOL.getValue() + ":  " + mDescribeProtocol +
                "\n" + "__________________________________________" +
                "\n" + AvailableCommandNames.PENDING_TROUBLE_CODES.getValue() + ":  " + mPendingTroubleCode;

    }

    public String toString2() {
        return "OBD DATA/DATOS OBD:" +
                "  " +
                "  " + AvailableCommandNames.SPEED.getValue() + ":  " +
                "  " +
                "  " + speed + " km/h" +
                "  " + "__________________________________________" +
                "  " + AvailableCommandNames.ENGINE_RPM.getValue() + ": " +
                "  " + " " +
                "  " + engineRpm +
                "  " + "__________________________________________" +
                "  " + AvailableCommandNames.ENGINE_RUNTIME.getValue() + ":  " +
                "  " + " " +
                "  " +  engineRuntime + "hh:mm:ss" +
                "  " + "__________________________________________" +
                "  " +  mFaultCodes +
                "  " + " " +
                "Idling Fuel Consumtion:" +
                "  " + " " +
                "  " +  getmIdlingFuelConsumption() + "Litre" +
                "  " + "__________________________________________" +
                "Driving Fuel Consumtion:" +
                "  " + " " +
                "  " + getmDrivingFuelConsumption() + " Litre" +
                "  " + "__________________________________________" +
                " Instant Fuel Consumtion:" +
                "  " + " " +
                "  " +  mInsFuelConsumption + " L/100km" +
                " " + "__________________________________________" +
                "driving maf: " +
                "  " + mDrivingMaf + " g/s" +
                "  " + "__________________________________________" +
                "idle maf: " +
                "  " +
                "  " +  mIdleMaf + " g/s" +
                "  " + "__________________________________________" +
                "  " + AvailableCommandNames.FUEL_TYPE.getValue() + ":  " +
                "  " + " " +
                "  "  + mFuelTypeValue +
                "  " + "__________________________________________" +
                "Rapid Acceleration Times: " +
                "  " + " " +
                "  " +  mRapidAccTimes  +
                "  " + "__________________________________________" +
                "Rapid Decleration Times: " +
                "  " +   mRapidDeclTimes +
                "  " + "__________________________________________" +
                "Max Rpm: " +
                "  " + engineRpmMax +
                "  " + "__________________________________________" +
                "Max Speed: " +
                "  " + speedMax + " km/h" +
                "  " + "__________________________________________" +
                "Driving Duration: " +
                "" + getDrivingDuration() + " minute" +
                "" + "__________________________________________" +
                "Idle Duration: " +
                "  " + getIdlingDuration() + " minute" +
                "  " + "__________________________________________" +
                "  " + AvailableCommandNames.DISTANCE_TRAVELED_AFTER_CODES_CLEARED.getValue() + ":  " +
                "  "  + getmDistanceTraveledAfterCodesCleared() +
                "  " + "__________________________________________" +
                " " + AvailableCommandNames.DISTANCE_TRAVELED_MIL_ON.getValue() + ":  " +
                "  "   + mDistanceTraveledMilOn +
                "  " + "__________________________________________" +
                "  " + AvailableCommandNames.INTAKE_MANIFOLD_PRESSURE.getValue() + ":  " +
                "  " + mIntakePressure + " kpa" +
                "  " + "__________________________________________" +
                "  " + AvailableCommandNames.AIR_INTAKE_TEMP.getValue() + ":  " +
                "  " + mIntakeAirTemp + " C" +
                "  " + "__________________________________________" +
                "  " + AvailableCommandNames.FUEL_CONSUMPTION_RATE.getValue() + ":  " +
                "  " + mFuelConsumptionRate + " L/h" +
                "  " + "__________________________________________" +
                "  " + AvailableCommandNames.FUEL_LEVEL.getValue() + ":  " +
                "  " + mFuelLevel +
                "  " + "__________________________________________" +
                "  " + AvailableCommandNames.FUEL_PRESSURE.getValue() + ":  " +
                "  " + mFuelPressure +
                "  " + "__________________________________________" +
                "  " + AvailableCommandNames.ENGINE_FUEL_RATE.getValue() + ":  " +
                "  " + mEngineFuelRate +
                "  " + "__________________________________________" +
                "  " + AvailableCommandNames.ENGINE_COOLANT_TEMP.getValue() + ":  " +
                "  " + mEngineCoolantTemp +
                "  " + "__________________________________________" +
                "  " + AvailableCommandNames.ENGINE_LOAD.getValue() + ":  " +
                "  " + mEngineLoad +
                "  " + "__________________________________________" +
                "  " + AvailableCommandNames.ENGINE_OIL_TEMP.getValue() + ":  " +
                "  " + mEngineOilTemp +
                "  " + "__________________________________________" +

                "  " + AvailableCommandNames.BAROMETRIC_PRESSURE.getValue() + ":  " + mBarometricPressure +
                "  " + "__________________________________________" +
                "  " + AvailableCommandNames.AIR_FUEL_RATIO.getValue() + ":  " + mAirFuelRatio +
                "  " + "__________________________________________" +
                "  " + AvailableCommandNames.WIDEBAND_AIR_FUEL_RATIO.getValue() + ":  " + mWideBandAirFuelRatio +
                "  " + "__________________________________________" +
                "  " + AvailableCommandNames.ABS_LOAD.getValue() + ":  " + mAbsLoad +
                "  " + "__________________________________________" +
                "  " + AvailableCommandNames.CONTROL_MODULE_VOLTAGE.getValue() + ":  " + mControlModuleVoltage +
                "  " + "__________________________________________" +
                "  " + AvailableCommandNames.EQUIV_RATIO.getValue() + ":  " + mEquivRatio +
                "  " + "__________________________________________" +
                "  " + AvailableCommandNames.DTC_NUMBER.getValue() + ":  " + mDtcNumber +
                "  " + "__________________________________________" +
                " " + AvailableCommandNames.DESCRIBE_PROTOCOL.getValue() + ":  " + mDescribeProtocol +
                "  " + "__________________________________________" +
                "  " + AvailableCommandNames.PENDING_TROUBLE_CODES.getValue() + ":  " + mPendingTroubleCode;

    }


}
