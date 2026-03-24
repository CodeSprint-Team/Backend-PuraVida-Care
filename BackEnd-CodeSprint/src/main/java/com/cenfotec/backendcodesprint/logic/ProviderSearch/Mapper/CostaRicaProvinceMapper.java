package com.cenfotec.backendcodesprint.logic.ProviderSearch.Mapper;

import java.util.List;
import java.util.Map;

public class CostaRicaProvinceMapper {

    public static final Map<String, List<String>> PROVINCE_CANTONS = Map.of(
            "San José", List.of(
                    "San José", "Escazú", "Desamparados", "Puriscal", "Tarrazú",
                    "Aserrí", "Mora", "Goicoechea", "Santa Ana", "Alajuelita",
                    "Vázquez de Coronado", "Acosta", "Tibás", "Moravia",
                    "Montes de Oca", "Turrubares", "Dota", "Curridabat",
                    "Pérez Zeledón", "León Cortés Castro"
            ),
            "Alajuela", List.of(
                    "Alajuela", "San Ramón", "Grecia", "San Mateo", "Atenas",
                    "Naranjo", "Palmares", "Poás", "Orotina", "San Carlos",
                    "Zarcero", "Sarchí", "Upala", "Los Chiles", "Guatuso",
                    "Río Cuarto"
            ),
            "Cartago", List.of(
                    "Cartago", "Paraíso", "La Unión", "Jiménez", "Turrialba",
                    "Alvarado", "Oreamuno", "El Guarco"
            ),
            "Heredia", List.of(
                    "Heredia", "Barva", "Santo Domingo", "Santa Bárbara",
                    "San Rafael", "San Isidro", "Belén", "Flores",
                    "San Pablo", "Sarapiquí"
            ),
            "Guanacaste", List.of(
                    "Liberia", "Nicoya", "Santa Cruz", "Bagaces", "Carrillo",
                    "Cañas", "Abangares", "Tilarán", "Nandayure",
                    "La Cruz", "Hojancha"
            ),
            "Puntarenas", List.of(
                    "Puntarenas", "Esparza", "Buenos Aires", "Montes de Oro",
                    "Osa", "Quepos", "Golfito", "Coto Brus", "Parrita",
                    "Corredores", "Garabito", "Monteverde"
            ),
            "Limón", List.of(
                    "Limón", "Pococí", "Siquirres", "Talamanca",
                    "Matina", "Guácimo"
            )
    );


    public static List<String> getCantonsForProvince(String province) {
        if (province == null || province.isBlank()) return List.of();
        return PROVINCE_CANTONS.getOrDefault(province, List.of());
    }


    public static boolean isProvince(String zone) {
        return zone != null && PROVINCE_CANTONS.containsKey(zone);
    }
}