package bit.config.graphql.config;

import bit.config.graphql.fetcher.MyDataFetchers;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.TypeRuntimeWiring;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GraphQLConfig {

    private final MyDataFetchers myDataFetchers;

    @Bean
    public RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(TypeRuntimeWiring.newTypeWiring("Query")
                        .dataFetcher("getAnniversary", myDataFetchers.getAnniversaryFetcher())
                        .dataFetcher("getListAnniversary", myDataFetchers.getListAnniversaryFetcher())
                        .dataFetcher("getAnniversariesInRange", myDataFetchers.getAnniversariesInRangeFetcher())
                        .dataFetcher("getMonthlyAnniversaries", myDataFetchers.getMonthlyAnniversariesFetcher())
                        .dataFetcher("getYearlyAnniversaries", myDataFetchers.getYearlyAnniversariesFetcher()))
                .type(TypeRuntimeWiring.newTypeWiring("Mutation")
                        .dataFetcher("createAnniversary", myDataFetchers.createAnniversaryFetcher())
                        .dataFetcher("updateAnniversary", myDataFetchers.updateAnniversaryFetcher())
                        .dataFetcher("deleteAnniversary", myDataFetchers.deleteAnniversaryFetcher()))
                .build();
    }
}
