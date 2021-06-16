package br.com.zup.edu.propostas

import br.com.zup.edu.CadastraPropostasRequest
import br.com.zup.edu.CadastraPropostasResponse
import br.com.zup.edu.PropostasGrpcServiceGrpc
import io.grpc.stub.StreamObserver
import java.math.BigDecimal
import javax.inject.Singleton

@Singleton
class PropostaGrpcEndpoint(val propostaRepository: PropostaRepository) :
    PropostasGrpcServiceGrpc.PropostasGrpcServiceImplBase() {

    override fun cadastra(
        request: CadastraPropostasRequest,
        responseObserver: StreamObserver<CadastraPropostasResponse>
    ) {
        println("Recebendo a request da pessoa ${request.nome}")

        val novaProposta = request.paraProposta()

        val propostaSalva = propostaRepository.save(novaProposta)

        val response = CadastraPropostasResponse.newBuilder()
            .setId(propostaSalva.id ?: throw IllegalStateException("valor do id nulo"))
            .build()
//
//        val response = CadastraPropostasResponse.newBuilder()
//            .setId(propostaSalva.id!!)
//            .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}

fun CadastraPropostasRequest.paraProposta() = Proposta(
    nome = this.nome,
    email = this.email,
    documento = this.documento,
    endereco = this.endereco,
    salario = BigDecimal(this.salario)
)