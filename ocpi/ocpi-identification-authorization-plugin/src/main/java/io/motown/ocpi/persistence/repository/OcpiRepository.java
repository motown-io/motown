/**
 * Copyright (C) 2013 Motown.IO (info@motown.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.motown.ocpi.persistence.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.motown.ocpi.persistence.entities.Endpoint;
import io.motown.ocpi.persistence.entities.Subscription;
import io.motown.ocpi.persistence.entities.Token;
import io.motown.ocpi.persistence.entities.TokenSyncDate;

/**
 * OcpiRepository
 * 
 * @author bartwolfs
 *
 */
@Repository
public class OcpiRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public List<Endpoint> findAllEndpoints() {
		try {
			return entityManager.createQuery("SELECT endpoint FROM Endpoint AS endpoint", Endpoint.class)
					.getResultList();

		} finally {
			entityManager.close();
		}
	}

	/**
	 * findAllSubscriptions
	 * 
	 * @return
	 */
	public List<Subscription> findAllSubscriptions() {
		try {
			return entityManager
					.createQuery("SELECT subscription FROM Subscription AS subscription", Subscription.class)
					.getResultList();

		} finally {
			entityManager.close();
		}
	}

	/**
	 * findSubscriptionByLukasAuthorizationToken
	 * 
	 * @param lukasAuthorizationToken
	 * @return
	 */
	public Subscription findSubscriptionByLukasAuthorizationToken(String lukasAuthorizationToken) {
		try {
			return entityManager
					.createQuery(
							"SELECT subscription FROM Subscription AS subscription WHERE lukasAuthorizationToken = :lukasAuthorizationToken",
							Subscription.class)
					.setParameter("lukasAuthorizationToken", lukasAuthorizationToken).setMaxResults(1)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			entityManager.close();
		}
	}

	/**
	 * findTokenByUid
	 * 
	 * @param uid
	 * @return
	 */
	public Token findTokenByUid(String uid) {
		try {
			return entityManager.createQuery("SELECT token FROM Token AS token WHERE uid = :uid", Token.class)
					.setParameter("uid", uid).setMaxResults(1).getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			entityManager.close();
		}
	}

	/**
	 * findTokenByUidAndIssuingCompany
	 * 
	 * @param uid
	 * @param issuingCompany
	 * @return
	 */
	public Token findTokenByUidAndIssuingCompany(String uid, String issuingCompany) {
		try {
			return entityManager
					.createQuery(
							"SELECT token FROM Token AS token WHERE uid = :uid and issuingCompany = :issuingCompany",
							Token.class)
					.setParameter("uid", uid).setParameter("issuingCompany", issuingCompany).setMaxResults(1)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			entityManager.close();
		}
	}

	/**
	 * findTokenByVisualNumber
	 * 
	 * @param visualNumber
	 * @return
	 */
	public Token findTokenByVisualNumber(String visualNumber) {
		try {
			return entityManager
					.createQuery("SELECT token FROM Token AS token WHERE visualNumber = :visualNumber", Token.class)
					.setParameter("visualNumber", visualNumber).setMaxResults(1).getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			entityManager.close();
		}
	}

	/**
	 * getTokenSyncDate
	 * 
	 * @return
	 */
	public TokenSyncDate getTokenSyncDate() {
		try {
			List<TokenSyncDate> list = entityManager
					.createQuery("SELECT syncDate FROM TokenSyncDate AS syncDate", TokenSyncDate.class).setMaxResults(1)
					.getResultList();
			if (!list.isEmpty()) {
				return list.get(0);
			}
			return null;

		} finally {
			entityManager.close();
		}
	}

	/**
	 * insertOrUpdate
	 * 
	 * @param subscription
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Subscription insertOrUpdate(Subscription subscription) {
		try {
			Subscription persistedSubscription;
			if (subscription.getId() == null) {
				entityManager.persist(subscription);
				persistedSubscription = subscription;
			} else {
				persistedSubscription = entityManager.merge(subscription);
			}
			entityManager.flush();
			return persistedSubscription;

		} finally {
			entityManager.close();
		}
	}

	/**
	 * insertOrUpdate
	 * 
	 * @param token
	 * @return
	 */
	@Transactional
	public Token insertOrUpdate(Token token) {
		try {
			Token persistedToken;
			if (token.getId() == null) {
				entityManager.persist(token);
				persistedToken = token;
			} else {
				persistedToken = entityManager.merge(token);
			}
			return persistedToken;

		} finally {
			entityManager.close();
		}
	}

	/**
	 * insertOrUpdate
	 * 
	 * @param tokenSyncDate
	 * @return
	 */
	@Transactional
	public TokenSyncDate insertOrUpdate(TokenSyncDate tokenSyncDate) {
		try {
			TokenSyncDate persistedTokenSyncDate;
			if (tokenSyncDate.getId() == null) {
				entityManager.persist(tokenSyncDate);
				persistedTokenSyncDate = tokenSyncDate;
			} else {
				persistedTokenSyncDate = entityManager.merge(tokenSyncDate);
			}
			return persistedTokenSyncDate;

		} finally {
			entityManager.close();
		}
	}

}
