
package com.codetrailer;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.Card;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.CardBuilder;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableNativeMap;

public class RNBraintreeModule extends ReactContextBaseJavaModule implements PaymentMethodNonceCreatedListener {
  private BraintreeFragment mBraintreeFragment;
  private Promise mPromise;
  private final ReactApplicationContext reactContext;

  public RNBraintreeModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNBraintree";
  }

  @Override
  public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
    WritableNativeMap options = new WritableNativeMap();
    options.putString("nonce", paymentMethodNonce.getNonce());
    options.putString("description", paymentMethodNonce.getDescription());

    mPromise.resolve(options);
  }

  @ReactMethod
  public void addToken(String key, Promise promise){
    try {
      mBraintreeFragment = BraintreeFragment.newInstance(getCurrentActivity(), key);
      mBraintreeFragment.addListener(this);
    } catch (InvalidArgumentException e) {
      e.printStackTrace();
    }

    promise.resolve(null);
  }

  @ReactMethod
  public void fetchNonce(ReadableMap options, Promise promise) {
    mPromise = promise;
    CardBuilder cardBuilder = new CardBuilder()
            .cardholderName(options.getString("name"))
            .expirationYear(options.getString("expYear"))
            .expirationMonth(options.getString("expMonth"))
            .cvv(options.getString("cvc"))
            .cardNumber(options.getString("number"));

    Card.tokenize(mBraintreeFragment, cardBuilder);
  }
}