package com.cineplex.system.service;

import com.cineplex.system.repository.ClienteRepository;

public class ClienteService {

    private final ClienteRepository clienteRepo = new ClienteRepository();

    public int crear(String nombre, String correo) {
        return clienteRepo.crear(nombre, correo);
    }
}
