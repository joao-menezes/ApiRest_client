package com.client.DsClient.service;

import com.client.DsClient.dto.ClientDTO;
import com.client.DsClient.entities.Client;
import com.client.DsClient.repositories.ClientRepository;
import com.client.DsClient.service.exceptions.DatabaseException;
import com.client.DsClient.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAllPaged(PageRequest pageRequest){
        Page<Client> list = clientRepository.findAll(pageRequest);
        return list.map(ClientDTO::new);
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id){
        Optional<Client> obj = clientRepository.findById(id);
        Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found or not exisit"));
        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO insertClient( ClientDTO dto){
        Client entity = new Client();
        inserUpdateClient(dto,entity);
        entity = clientRepository.save(entity);

        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO updateClient(Long id,ClientDTO dto){
        try {
            Client entity = clientRepository.getById(id);
            inserUpdateClient(dto,entity);
            entity = clientRepository.save(entity);

            return new ClientDTO(entity);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Entity not found, ID: "+id);
        }
    }


    @Transactional
    public void deleteClient(Long id){
        try {
            clientRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Entity not found, id: " + id);
        }catch (DataIntegrityViolationException e){
            throw new DatabaseException("Integrity Violation" + e.getMessage());
        }
    }

        public void inserUpdateClient(ClientDTO dto, Client entity){
            entity.setName(dto.getName());
            entity.setBirthDate(dto.getBirthDate());
            entity.setCpf(dto.getCpf());
            entity.setIncome(dto.getIncome());
            entity.setChildren(dto.getChildren());
        }





}
