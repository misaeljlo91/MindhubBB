package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountCard;
import com.mindhub.homebanking.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AccountCardRepository extends JpaRepository<AccountCard, Long> {
    AccountCard findByAccount(Account account);
    AccountCard findByCard(Card card);
}
