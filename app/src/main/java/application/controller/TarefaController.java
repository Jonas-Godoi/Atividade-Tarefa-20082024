package application.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import application.model.Tarefa;
import application.repository.TarefaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;



@RestController
@RequestMapping("/tarefas")
public class TarefaController {
        @Autowired
        private TarefaRepository tarefaRepo;

        @GetMapping
        public Iterable<Tarefa> list(){
            return tarefaRepo.findAll();
        }

        @PostMapping
        public Tarefa insert(@RequestBody Tarefa al){
            return tarefaRepo.save(al);
        }

        @GetMapping("/{id}")
        public Tarefa details(@PathVariable long id){
            Optional<Tarefa> resultado = tarefaRepo.findById(id);
            if (resultado.isEmpty()) {
                throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,"Tarefa não encontrada"
                );
            }
            return tarefaRepo.findById(id).get();
        }

        @PutMapping("/{id}")
        public Tarefa put(
            @PathVariable long id,
            @RequestBody Tarefa novosDados){

            Optional<Tarefa> resultado =  tarefaRepo.findById(id);
            if (resultado.isEmpty()) {
                throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Tarefa não encontrada"
                );
            }
            if(novosDados.getDescricao().isEmpty()){
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"Descriçao invalida"
                );
            }
            if (novosDados.getConcluido() == null) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "O campo 'concluido' deve ser verdadeiro ou falso."
                );
            }

            Tarefa tarefaExistente = resultado.get();
            tarefaExistente.setDescricao(novosDados.getDescricao());
            tarefaExistente.setConcluido(novosDados.getConcluido());

            return tarefaRepo.save(resultado.get());
        }

        @DeleteMapping("/{id}")
        public void delete(@PathVariable long id){
            if(!tarefaRepo.existsById(id)){
                throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,"Tarefa não encontrada"
                );
            }
            tarefaRepo.deleteById(id);
        }
    
}