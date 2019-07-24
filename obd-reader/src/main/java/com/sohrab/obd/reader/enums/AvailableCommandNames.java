package com.sohrab.obd.reader.enums;

import app.com.android_obd_reader.R;

import static app.com.android_obd_reader.R.string.engineRPM;

/**
 * Names of all available commands.
 *
 * @author pires
 * @version $Id: $Id
 */
public enum AvailableCommandNames {

    AIR_INTAKE_TEMP("Air Intake Temperature/Temperatura de admision de aire"),
    AMBIENT_AIR_TEMP("Ambient Air Temperature/Temperatura del aire ambiente"),
    ENGINE_COOLANT_TEMP("Engine Coolant Temperatura/Temperatura del refrigerante del motor"),
    BAROMETRIC_PRESSURE("Barometric Pressure/Presion barometrica"),
    FUEL_PRESSURE("Fuel Pressure/Presion de combustible"),
    INTAKE_MANIFOLD_PRESSURE("Intake Manifold Pressure/Presion del colector de admision"),
    ENGINE_LOAD("Engine Load/Carga del motor"),
    ENGINE_RUNTIME("Engine Runtime/Motor runtime"),
    ENGINE_RPM("Engine RPM/RPM del Motor"),
    SPEED("Vehicle Speed/Velocidad del vehiculo"),
    MAF("Mass Air Flow/Flujo de aire masa"),
    THROTTLE_POS("Throttle Position/Posicion del acelerador"),
    TROUBLE_CODES("Trouble Codes/Codigos de problema"),
    PENDING_TROUBLE_CODES("Pending Trouble Codes/Codigos de problemas pendientes"),
    PERMANENT_TROUBLE_CODES("Permanent Trouble Codes/Codigos de problemas permanentes"),
    FUEL_LEVEL("Fuel Level/Nivel de combustible"),
    FUEL_TYPE("Fuel Type/Tipo de combustible"),
    FUEL_CONSUMPTION_RATE("Fuel Consumption Rate/Tasa de consumo de combustible"),
    TIMING_ADVANCE("Timing Advance/Avance de tiempo"),
    DTC_NUMBER("Diagnostic Trouble Codes/Codigos de diagnostico de problemas"),
    EQUIV_RATIO("Command Equivalence Ratio/Relacion de equivalencia de comando"),
    DISTANCE_TRAVELED_AFTER_CODES_CLEARED("Distance since codes cleared/Distancia desde que se borraron los codigos"),
    CONTROL_MODULE_VOLTAGE("Control Module Power Supply/Fuente de alimentacion del moduolo de control"),
    ENGINE_FUEL_RATE("Engine Fuel Rate/Tasa de combustible del motor"),
    FUEL_RAIL_PRESSURE("Fuel Rail Pressure/Presion del riel de combustible"),
    VIN("Vehicle Identification Number (VIN)/Numero de ID del vehiculo"),
    DISTANCE_TRAVELED_MIL_ON("Distance traveled with MIL on/Distancia recorrida con MILL en"),
    TIME_TRAVELED_MIL_ON("Time run with MIL on"),
    TIME_SINCE_TC_CLEARED("Time since trouble codes cleared"),
    REL_THROTTLE_POS("Relative throttle position"),
    PIDS_01_20("Available PIDs 01-20"),
    PIDS_21_40("Available PIDs 21-40"),
    PIDS_41_60("Available PIDs 41-60"),
    ABS_LOAD("Absolute load"),
    ENGINE_OIL_TEMP("Engine oil temperature/Temperatura del aceite del motor"),
    AIR_FUEL_RATIO("Air/Fuel Ratio"),
    WIDEBAND_AIR_FUEL_RATIO("Wideband Air/Fuel Ratio"),
    DESCRIBE_PROTOCOL("Describe protocol"),
    DESCRIBE_PROTOCOL_NUMBER("Describe protocol number"),
    IGNITION_MONITOR("Ignition monitor")
    ;

    private final String value;

    /**
     * @param value Command description
     */
    AvailableCommandNames(String value) {
        this.value = value;
    }

    /**
     * <p>Getter for the field <code>value</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public final String getValue() {
        return value;
    }

}
