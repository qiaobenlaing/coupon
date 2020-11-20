//
//  BMSQ_StoreQrcodeViewController.m
//  BMSQS
//
//  Created by lxm on 15/8/9.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_StoreQrcodeViewController.h"
#import "UIImageView+WebCache.h"
#import "ZXingObjC.h"
#import <CommonCrypto/CommonCryptor.h>
#import <CommonCrypto/CommonDigest.h>
#import "GTMDefines.h"
#import "GTMBase64.h"
#import "JSONKit.h"

const char ThreeDeskey[] = {
    0x11, 0x22, 0x4F, 0x58,
    0x88, 0x10, 0x40, 0x38,
    0x28, 0x25, 0x79, 0x51,
    0xCB, 0xDD, 0x55, 0x66,
    0x77, 0x29, 0x74, 0x98,
    0x30, 0x40, 0x36, 0xE2
};
@interface BMSQ_StoreQrcodeViewController ()

@end

@implementation BMSQ_StoreQrcodeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.navigationItem setTitle:@"我的二维码"];
    // Do any additional setup after loading the view.
    [self createModel];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

- (void)createModel
{
    if(IsNOTNullOrEmptyOfDictionary(self.shopAllMsg)){
        NSDictionary *dic = [self.shopAllMsg objectForKey:@"shopInfo"];
        NSString *imgStr = [NSString stringWithFormat:@"%@/%@",APP_SERVERCE_HOME,[dic objectForKey:@"logoUrl"]];
        [_logoImageView sd_setImageWithURL:[NSURL URLWithString:imgStr]];
        
        _nameLabel.text =  [dic objectForKey:@"shopName"];
        _addressLabel.text =  [dic objectForKey:@"street"];
        
    }
    Byte byteaa[] = {0x11, 0x22, 0x4F, 0x58,
        0x88, 0x10, 0x40, 0x38,
        0x28, 0x25, 0x79, 0x51,
        0xCB, 0xDD, 0x55, 0x66,
        0x77, 0x29, 0x74, 0x98,
        0x30, 0x40, 0x36, 0xE2};
    
    NSData *adata = [[NSData alloc] initWithBytes:byteaa length:24];
    NSString *aString = [[NSString alloc] initWithData:adata encoding:NSASCIIStringEncoding];
    NSLog(@"aString=%@",aString);
    
    NSString *qType = @"qr001";
    NSString *shopCode = [gloabFunction getShopCode];
    NSString *sSrc = [shopCode substringWithRange:NSMakeRange(shopCode.length-6,6)];


    NSString *sCode = [self TripleDES:sSrc encryptOrDecrypt:kCCEncrypt encryptOrDecryptKey:aString withaa:byteaa];

    NSLog(@"%@",sCode);

    
    NSDictionary *dic =@{@"shopCode":shopCode,
                         @"sSrc":sSrc,
                         @"sCode":sCode,
                         @"qType":qType};
    
    NSError *error = nil;
    ZXMultiFormatWriter *writer = [ZXMultiFormatWriter writer];
    ZXBitMatrix* result = [writer encode:[dic JSONString]
                                  format:kBarcodeFormatQRCode
                                   width:500
                                  height:500
                                   error:&error];
    if (result) {
        CGImageRef imageT = [[ZXImage imageWithMatrix:result] cgimage];
        UIImage* imageF = [UIImage imageWithCGImage: imageT];
        _qrcodeImageView.image = imageF;
    } else {
        NSString *errorMessage = [error localizedDescription];
    }
}

//+(NSData*) hexToBytes:(NSString *)str
//{
//    NSMutableData* data = [NSMutableData data];
//    int idx;
//    for (idx = 0; idx+2 <= str.length; idx+=2) {
//        NSRange range = NSMakeRange(idx, 2);
//        NSString* hexStr = [str substringWithRange:range];
//        NSScanner* scanner = [NSScanner scannerWithString:hexStr];
//        unsigned int intValue;
//        [scanner scanHexInt:&intValue;];
//        [data appendBytes:&intValue; length:1];
//        
//    }
//    return data;
//}
//
//+ (NSString *)hexStringFromData:(NSData *)data
//{
//    NSMutableString *str = [NSMutableString string];
//    
//    Byte *byte = (Byte *)[data bytes];
//    
//    for (int i = 0; i<[data length]; i++) {
//        
//        // byte+i为指针
//        [str appendString:[self stringFromByte:*(byte+i)]];
//        
//    }
//    return str;
//}
//
//
//+ (NSString *)stringFromByte:(Byte)byteVal
//{
//    NSMutableString *str = [NSMutableString string];
//    //取高四位
//    
//    Byte byte1 = byteVal>>4;
//    //取低四位 
//    
//    Byte byte2 = byteVal & 0xf; 
//    
//    //拼接16进制字符串 
//    [str appendFormat:@"%x",byte1]; 
//    
//    [str appendFormat:@"%x",byte2]; 
//    
//    return str; 
//}

-(NSString *)TripleDES:(NSString *)plainText encryptOrDecrypt:(CCOperation)encryptOrDecrypt encryptOrDecryptKey:(NSString *)encryptOrDecryptKey withaa:(const void *)vkeyaa
{
    
    const void *vplainText;
    size_t plainTextBufferSize;
    
    if (encryptOrDecrypt == kCCDecrypt)//解密
    {
        NSData *EncryptData = [GTMBase64 decodeData:[plainText dataUsingEncoding:NSUTF8StringEncoding]];
        plainTextBufferSize = [EncryptData length];
        vplainText = [EncryptData bytes];
    }
    else //加密
    {
        NSData* data = [plainText dataUsingEncoding:NSUTF8StringEncoding];
        plainTextBufferSize = [data length];
        vplainText = (const void *)[data bytes];
    }
    
    CCCryptorStatus ccStatus;
    uint8_t *bufferPtr = NULL;
    size_t bufferPtrSize = 0;
    size_t movedBytes = 0;
    
    bufferPtrSize = (plainTextBufferSize + kCCBlockSize3DES) & ~(kCCBlockSize3DES - 1);
    bufferPtr = malloc( bufferPtrSize * sizeof(uint8_t));
    memset((void *)bufferPtr, 0x0, bufferPtrSize);
    // memset((void *) iv, 0x0, (size_t) sizeof(iv));
    
    const void *vkey = (const void *)[encryptOrDecryptKey UTF8String];
    // NSString *initVec = @"init Vec";
    //const void *vinitVec = (const void *) [initVec UTF8String];
    //  Byte iv[] = {0x12, 0x34, 0x56, 0x78, 0x90, 0xAB, 0xCD, 0xEF};
    ccStatus = CCCrypt(encryptOrDecrypt,
                       kCCAlgorithm3DES,
                       kCCOptionPKCS7Padding | kCCOptionECBMode,
                       vkeyaa,
                       kCCKeySize3DES,
                       nil,
                       vplainText,
                       plainTextBufferSize,
                       (void *)bufferPtr,
                       bufferPtrSize,
                       &movedBytes);
    //if (ccStatus == kCCSuccess) NSLog(@"SUCCESS");
    /*else if (ccStatus == kCC ParamError) return @"PARAM ERROR";
     else if (ccStatus == kCCBufferTooSmall) return @"BUFFER TOO SMALL";
     else if (ccStatus == kCCMemoryFailure) return @"MEMORY FAILURE";
     else if (ccStatus == kCCAlignmentError) return @"ALIGNMENT";
     else if (ccStatus == kCCDecodeError) return @"DECODE ERROR";
     else if (ccStatus == kCCUnimplemented) return @"UNIMPLEMENTED"; */
    
    NSString *result;
    
    if (encryptOrDecrypt == kCCDecrypt)
    {
        result = [[NSString alloc] initWithData:[NSData dataWithBytes:(const void *)bufferPtr
                                                               length:(NSUInteger)movedBytes]
                                       encoding:NSUTF8StringEncoding];
    }
    else
    {
        NSData *myData = [NSData dataWithBytes:(const void *)bufferPtr length:(NSUInteger)movedBytes];
        result = [GTMBase64 stringByEncodingData:myData];
    }
    
    return result;
}

@end
