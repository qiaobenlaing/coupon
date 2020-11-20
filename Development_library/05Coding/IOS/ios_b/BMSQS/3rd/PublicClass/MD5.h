//
//  MD5.h
//  IcardEnglish
//
//  Created by djx on 12-6-7.
//  Copyright 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CommonCrypto/CommonDigest.h>

@interface MD5 : NSObject 
{

}

+(NSString*)MD5Value:(NSString*)strDes;
@end
