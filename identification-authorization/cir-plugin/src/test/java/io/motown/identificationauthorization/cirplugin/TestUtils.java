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
package io.motown.identificationauthorization.cirplugin;

import io.motown.domain.api.chargingstation.IdentifyingToken;
import io.motown.domain.api.chargingstation.TextualToken;
import io.motown.identificationauthorization.cirplugin.cir.schema.ArrayOfCard;
import io.motown.identificationauthorization.cirplugin.cir.schema.Card;
import io.motown.identificationauthorization.cirplugin.cir.schema.InquireResult;

public class TestUtils {

    public static IdentifyingToken getIdentifyingToken() {
        return new TextualToken("token");
    }

    public static ArrayOfCard getArrayOfCard(IdentifyingToken token) {
        Card card = new Card();
        card.setCardID(token.getToken());

        ArrayOfCard arrayOfCard = new ArrayOfCard();
        arrayOfCard.getCard().add(card);

        return arrayOfCard;
    }

    public static InquireResult getInquireResult(boolean valid) {
        InquireResult result = new InquireResult();

        ArrayOfCard arrayOfCard = getArrayOfCard(getIdentifyingToken());
        arrayOfCard.getCard().get(0).setValid(valid);
        result.setCards(arrayOfCard);

        return result;
    }

    public static InquireResult getInquireResultValidWithError() {
        InquireResult result = getInquireResult(true);

        io.motown.identificationauthorization.cirplugin.cir.schema.Error error = new io.motown.identificationauthorization.cirplugin.cir.schema.Error();
        error.setErrorCode("1");
        error.setErrorText("Error");
        result.setError(error);

        return result;
    }

    public static String getCirServiceUsername() {
        return "username";
    }

    public static String getCirServicePassword() {
        return "password";
    }

}
