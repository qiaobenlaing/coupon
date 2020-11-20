//
//  MD5.m
//  IcardEnglish
//
//  Created by djx on 12-6-7.
//  Copyright 2012 __MyCompanyName__. All rights reserved.
//

#import "MD5.h"


@implementation MD5

+(NSString*)MD5Value:(NSString*)strDes
{
	const char* str = [strDes UTF8String];
    unsigned char result[CC_MD5_DIGEST_LENGTH];
    CC_MD5(str, strlen(str), result);
    NSMutableString *ret = [NSMutableString stringWithCapacity:CC_MD5_DIGEST_LENGTH];
    
    for(int i = 0; i<CC_MD5_DIGEST_LENGTH; i++) {
        [ret appendFormat:@"%02x",result[i]];
    }    
    return ret;
}
@end
