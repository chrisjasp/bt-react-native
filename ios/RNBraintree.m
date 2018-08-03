#import "RNBraintree.h"
#import "BraintreeCard.h"

// From .configure
NSString* authorizationToken;

@implementation RNBraintree

RCT_REMAP_METHOD(addToken,
                 token:(NSString *)token
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    authorizationToken = token;

    resolve([NSNull null]);
}

RCT_REMAP_METHOD(fetchNonce,
                 options:(NSDictionary*)options
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    // Create Braintree client (using previously obtained authorization token)
    BTAPIClient *braintreeClient = [[BTAPIClient alloc] initWithAuthorization:authorizationToken];
    BTCardClient *cardClient = [[BTCardClient alloc] initWithAPIClient:braintreeClient];

    // Create Card record (using passed data)
    // NSDictionary* cardData = (NSDictionary*)[options objectForKey:@"card"];
    BTCard *card = [[BTCard alloc] initWithNumber:(NSString*)[options objectForKey:@"number"]
                                  expirationMonth:(NSString*)[options objectForKey:@"expirationMonth"]
                                   expirationYear:(NSString*)[options objectForKey:@"expirationYear"]
                                              cvv:(NSString*)[options objectForKey:@"cvv"]];

    card.cardholderName = [options objectForKey:@"name"];

    // Tokenize Card
    [cardClient tokenizeCard:card
                  completion:^(BTCardNonce *tokenizedCard, NSError *error) {
                      // Communicate the tokenizedCard.nonce to your server, or handle error
                      if (error != nil) {
                          reject(@"E_TOKEN", @"Failed to tokenize card", error);
                      } else {
                          resolve(@{@"last2": tokenizedCard.lastTwo,
                                    @"type": tokenizedCard.type,
                                    @"description": tokenizedCard.localizedDescription,
                                    @"nonce": tokenizedCard.nonce});
                      }
                  }];
}

RCT_EXPORT_MODULE();
@end