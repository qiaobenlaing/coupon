//
//  BMSQ_PayCardViewController.m
//  BMSQC
//
//  Created by liuqin on 15/9/2.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_PayCardViewController.h"

 #import "BMSQ_AddBankCardController.h"
#import "BMSQ_MyBankCardController.h"
#import "ZCTradeView.h"

#import "SVProgressHUD.h"

#import "BMSQ_PayRusultViewController.h"

#import "ZXingObjC.h"
#import <CommonCrypto/CommonCryptor.h>
#import <CommonCrypto/CommonDigest.h>
#import "GTMDefines.h"
#import "GTMBase64.h"
#import "JSONKit.h"

#import "NyfInsertsLabel.h"
#import "UIColor+Tools.h"
#import "MD5.h"


#import "ZCTradeView.h"

@interface BMSQ_PayCardViewController ()<UITextFieldDelegate,UIAlertViewDelegate>

@property (nonatomic, strong)UITableView *m_tableView;



@property(nonatomic, strong)NSArray *section0;
@property(nonatomic, strong)NSArray *section1;
//
//
@property(nonatomic, strong)NSArray *cardArray;
//
@property(nonatomic, strong)NSDictionary *seleCardDic;
//
//
//@property(nonatomic, strong)NSString *telePhone;
//@property(nonatomic, strong)NSString *valCode;  //确认订单时使用


//@property (nonatomic, strong)NSString *orderNbr;  //订单号
//@property (nonatomic, strong)NSString *consumeCode; //取消订单时用到




@property (nonatomic,assign)BOOL isNewPrice;
@property (nonatomic,assign)BOOL isCard;
@property (nonatomic,assign)BOOL isCardPay;

@end

@implementation BMSQ_PayCardViewController

-(void)keyboardWillShow{
    self.m_tableView.contentOffset = CGPointMake(0, 200);
}
-(void)keyboardHide{
    self.m_tableView.contentOffset = CGPointMake(0, 0);
}
- (void)viewDidLoad {
    [super viewDidLoad];

    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"工行惠支付"];
    
    
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWillShow)
                                                 name:UIKeyboardWillShowNotification
                                               object:nil];

    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardHide)
                                                 name:UIKeyboardDidHideNotification
                                               object:nil];
    
    UIButton* btnback = [UIButton buttonWithType:UIButtonTypeCustom];
    btnback.frame = CGRectMake(APP_VIEW_WIDTH - 64, 20, 64, 44);
    [btnback setTitle:@"确认支付" forState:0];
    btnback.titleLabel.font = [UIFont boldSystemFontOfSize:15];
    [btnback addTarget:self action:@selector(clickRight) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:btnback];
    
  
    self.section0 = @[@"订单号",@"商户名称",@"支付金额",@"银行卡后四位"];
    self.cardArray = [[NSMutableArray alloc]init];
    self.section1 = @[@"手机号",@"验证码"];

    self.m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT) style:UITableViewStyleGrouped];
    
    self.m_tableView.dataSource = self;
    self.m_tableView.delegate = self;
    self.m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:self.m_tableView];
    
    if (self.isBuy) {
        [self addCouponOrder];

    }else{
        if (!self.type56) {
            [self getNewPrice];  //第一步
            if (self.isTakeout) {  //第二步
                [self pOBankcardPay];
            }else{
                [self bankcardPay];
            }
            
        }else{
            self.isNewPrice = YES;
            self.isCardPay = YES;
            
        }
    }
    
    
    
    [self loadCard];

    [SVProgressHUD showWithStatus:@""];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(seleCard:) name:@"seleCard" object:nil];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(addCard:) name:@"addCard" object:nil];

    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(removeActiviy) name:@"removeActiviy" object:nil];
    
    //输入支付验证码选择银行卡
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(seleCard_val) name:@"seleCard_val" object:nil];

//输入支付验证码完成
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(verification:) name:@"overNum" object:nil];


}

-(void)removeActiviy{
    
    if (self.isNewPrice && self.isCardPay && self.isCard) {
        [SVProgressHUD dismiss];
    }
    
    if (self.isBuy) {
        [SVProgressHUD dismiss];

    }
    
}
-(void)seleCard_val{
    
    [[NSNotificationCenter defaultCenter] postNotificationName:@"ZCTradeInputViewOkButtonClick" object:self userInfo:nil];

    
        BMSQ_MyBankCardController *vc = [[BMSQ_MyBankCardController alloc]init];
        vc.ispayCard = YES;
        [self.navigationController pushViewController:vc animated:YES];
    
}

-(void)seleCard:(NSNotification *)not{
    
    self.seleCardDic = not.object;
    [self.m_tableView reloadData];
    
}

-(CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    return 5;
}
-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 5;
}
-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 5)];
    bgView.backgroundColor = [UIColor clearColor];
    return bgView;
}
-(UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 5)];
    bgView.backgroundColor = [UIColor clearColor];
    return bgView;
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 4;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    if (section ==0) {
        return  self.seleCardDic? self.section0.count:self.section0.count-1;
    }else if (section ==1) {
        NSUserDefaults *userD = [NSUserDefaults standardUserDefaults];
        NSString *str = [userD objectForKey:APP_USER_FREEVALCODEPAY];
        
        
        if(self.seleCardDic){
            return [str isEqualToString:@"1"]&&[self.payNewPrice floatValue]<=300?0:2;
        }else{
            return 0;
        }
        
        

            
        
    }else if (section ==2){
        NSUserDefaults *userD = [NSUserDefaults standardUserDefaults];
        NSString *str = [userD objectForKey:APP_USER_FREEVALCODEPAY];

        if([str isEqualToString:@"1"]&&[self.payNewPrice floatValue]<=300){
            return 0;
        }else{
            return self.cardArray.count>1?1:1;
        }
        
        
    }else{
        return 1;
    }
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.section ==0) {
        if (indexPath.row ==0) {
            return 110;
        }
        
    }
    return TABLEVIEWCELLH;
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    
    if (indexPath.section ==0) {
        
        
        static NSString *identifer = @"CardPayCell";
        
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifer];
        if (cell == nil) {
            
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifer];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 100, TABLEVIEWCELLH-1)];
            leftLabel.textAlignment = NSTextAlignmentLeft;
            leftLabel.font = [UIFont systemFontOfSize:13.f];
            leftLabel.backgroundColor = [UIColor whiteColor];
            leftLabel.textColor = [UIColor blackColor];
            leftLabel.tag = 88;
            [cell.contentView addSubview:leftLabel];
            
            NyfInsertsLabel *riginLabel = [[NyfInsertsLabel alloc]initWithFrame:CGRectMake(100, 0, APP_VIEW_WIDTH-100, TABLEVIEWCELLH-1) andInsets:UIEdgeInsetsMake(0, 0, 0, 10)];
            
            riginLabel.backgroundColor = [UIColor whiteColor];
            riginLabel.textAlignment = NSTextAlignmentRight;
            riginLabel.font = [UIFont systemFontOfSize:12.f];
            riginLabel.textColor = [UIColor blackColor];
            riginLabel.tag = 99;
            [cell.contentView addSubview:riginLabel];
            UIView *couponCodeQRView = [[UIView alloc]initWithFrame:CGRectMake(0, TABLEVIEWCELLH-10, APP_VIEW_WIDTH, 66)];
            couponCodeQRView.hidden = YES;
            couponCodeQRView.tag = 101;
            couponCodeQRView.backgroundColor = [UIColor whiteColor];
            [cell.contentView addSubview:couponCodeQRView];

            
            
            UIImageView *couponCodeQR = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 60)];
            couponCodeQR.tag = 200;
            couponCodeQR.backgroundColor = [UIColor whiteColor];
            [couponCodeQRView addSubview:couponCodeQR];

            
        }
        
        UILabel *left = (UILabel *)[cell viewWithTag:88];
        NyfInsertsLabel *rigin = (NyfInsertsLabel *)[cell viewWithTag:99];
        UIView *qrImageView = (UIView *)[cell viewWithTag:101];
        UIView *lineView = (UIView *)[cell viewWithTag:100];
        UIImageView *couponCodeQR = (UIImageView *)[cell viewWithTag:200];

        lineView.frame =CGRectMake(0, 49, [[UIScreen mainScreen]bounds].size.width, 0.5);
        left.text = [NSString stringWithFormat:@"  %@ ",[self.section0 objectAtIndex:indexPath.row]];
        
        CGSize size = [left.text boundingRectWithSize:CGSizeMake(MAXFLOAT, MAXFLOAT)
                                              options:NSStringDrawingUsesLineFragmentOrigin
                                           attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:13.f]}
                                              context:nil].size;
        
        left.frame = CGRectMake(0, 0, size.width, TABLEVIEWCELLH-1);
        rigin.frame = CGRectMake(size.width, 0, APP_VIEW_WIDTH-size.width, TABLEVIEWCELLH-1);

        
        if (indexPath.row ==0) {
            rigin.text =[NSString stringWithFormat:@": %@",self.orderNbr];
            rigin.textAlignment = NSTextAlignmentLeft;
            qrImageView.hidden = NO;
            
            NSError *error = nil;
            ZXMultiFormatWriter *writer = [ZXMultiFormatWriter writer];
            ZXBitMatrix* result = [writer encode:rigin.text
                                          format:kBarcodeFormatCode128
                                           width:200
                                          height:25
                                           error:&error];
            
            
            if (result) {
                CGImageRef imageT = [[ZXImage imageWithMatrix:result] cgimage];
                UIImage* imageF = [UIImage imageWithCGImage: imageT];
                [couponCodeQR setImage:imageF];
            }
            lineView.frame =CGRectMake(0, 100, [[UIScreen mainScreen]bounds].size.width, 0.5);



        }else if (indexPath.row ==1){
            qrImageView.hidden = YES;
            NSString *shopName =self.shopName;
            rigin.text = [NSString stringWithFormat:@"%@  ",shopName.length>0?shopName:@""];

        }else if (indexPath.row ==2){
            qrImageView.hidden = YES;
            rigin.textAlignment = NSTextAlignmentRight;
            rigin.text = [NSString stringWithFormat:@"%@  ",self.payNewPrice.length>0?self.payNewPrice:@""];
        }else if (indexPath.row ==3){
            qrImageView.hidden = YES;
            if ([self.seleCardDic objectForKey:@"accountNbrLast4"]) {
                NSString *accountNbrLast4 = [self.seleCardDic objectForKey:@"accountNbrLast4"];
                rigin.text = [NSString stringWithFormat:@"%@  ",accountNbrLast4.length>0?accountNbrLast4:@""];
            }
        }
        
        return cell;
    }else if (indexPath.section == 1){
        static NSString *identifer = @"CardPayCell1";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifer];
        if (cell == nil) {
            
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifer];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;

            UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, [[UIScreen mainScreen]bounds].size.width/4, TABLEVIEWCELLH-1)];
            leftLabel.textAlignment = NSTextAlignmentLeft;
            leftLabel.font = [UIFont systemFontOfSize:14.f];
            leftLabel.backgroundColor = [UIColor whiteColor];
            leftLabel.textColor = [UIColor blackColor];
            leftLabel.tag = 88;
            [cell.contentView addSubview:leftLabel];
            
          UITextField  *myTextField = [[UITextField alloc]initWithFrame:CGRectMake(leftLabel.frame.size.width, 0,APP_VIEW_WIDTH-leftLabel.frame.size.width, TABLEVIEWCELLH-1)];
            myTextField.font = [UIFont systemFontOfSize:14.f];
            myTextField.textColor = [UIColor blackColor];
            myTextField.backgroundColor = [UIColor whiteColor];
            myTextField.tag = 99;
            myTextField.delegate = self;
            myTextField.keyboardType = UIKeyboardTypeDefault;
            myTextField.returnKeyType = UIReturnKeyDone;
            [myTextField addTarget:self action:@selector(changeText:) forControlEvents:UIControlEventEditingChanged];
            [cell.contentView addSubview:myTextField];
            
            UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-100, 10, 90, 30)];
            [button setTitle:@"获取验证码" forState:UIControlStateNormal];
            [button setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
            button.layer.masksToBounds = YES;
            [button addTarget:self action:@selector(getIcbcPayValCode:) forControlEvents:UIControlEventTouchUpInside];
            button.titleLabel.font = [UIFont systemFontOfSize:12.f];
            button.layer.cornerRadius = 3;
            button.layer.borderWidth = 0.5;
            button.tag = 111;
            [cell.contentView addSubview:button];
            
          
            
        }
        UIButton *button =(UIButton *) [cell.contentView viewWithTag:111];
        UILabel *left = (UILabel *)[cell viewWithTag:88];
        UITextField *myTextField = (UITextField *)[cell viewWithTag:99];
        left.text = [NSString stringWithFormat:@"  %@ ",[self.section1 objectAtIndex:indexPath.row]];

        if (indexPath.row ==0) {
            button.hidden = NO;
            
          
            
            myTextField.text = [self.seleCardDic objectForKey:@"mobileNbr"];
            myTextField.enabled = NO;
            
        }else{
            button.hidden = YES;
            myTextField.enabled = YES;

        }
        
        return cell;
        
    }else if (indexPath.section == 2){
        
        static NSString *identifer = @"CardPayCell2";

        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifer];
        if (cell == nil) {
            
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifer];
            cell.backgroundColor = [UIColor whiteColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;

            UIImageView *logoImage = [[UIImageView alloc]initWithFrame:CGRectMake(5, 10, 30, 30)];
            [logoImage setImage:[UIImage imageNamed:@"iv_ICBC"]];
            logoImage.tag = 100;
            logoImage.hidden = YES;
            [cell.contentView addSubview:logoImage];
            
            
            UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(40, 0, 300, TABLEVIEWCELLH-1)];
            leftLabel.textAlignment = NSTextAlignmentLeft;
            leftLabel.font = [UIFont systemFontOfSize:14.f];
            leftLabel.textColor = [UIColor blackColor];
            leftLabel.tag = 88;
            [cell.contentView addSubview:leftLabel];
            
            UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake([[UIScreen mainScreen]bounds].size.width-30, 10, 20, 30)];
            [button setImage:[UIImage imageNamed:@"iv_jtRight"] forState:UIControlStateNormal];
            button.tag = 222;
            button.backgroundColor = [UIColor clearColor];
            [cell.contentView addSubview:button];

        }
        UIImageView *ICBCImg = (UIImageView *)[cell viewWithTag:100];
        UILabel *left = (UILabel *)[cell viewWithTag:88];
        if(self.cardArray.count >0){
            left.text =  [NSString stringWithFormat:@"%@  尾号%@",[self.seleCardDic objectForKey:@"bankName"],[self.seleCardDic objectForKey:@"accountNbrLast4"]];
            ICBCImg.hidden = NO;
        }else{
            left.text = @"未绑定银行卡，快去绑定吧～～";
            ICBCImg.hidden = YES;
            
        }
        
        
        
        return cell;
        
    }else{
        static NSString *identifier = @"orderSubmitCell";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell == nil) {
            
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, [[UIScreen mainScreen]bounds].size.width-20, 40)];
            button.layer.cornerRadius = 6;
            button.layer.masksToBounds = YES;
            [button setTitle:@"确认支付" forState:UIControlStateNormal];
            button.backgroundColor = [UIColor colorWithRed:182/255.0 green:0 blue:12/255.0 alpha:1];
            [button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
            [button addTarget:self action:@selector(goPay) forControlEvents:UIControlEventTouchUpInside];
            button.center = CGPointMake([[UIScreen mainScreen]bounds].size.width/2, 25);
            button.tag =100;
            [cell.contentView addSubview: button];
            
            
        }
        
        UIButton *subButton = (UIButton *)[cell.contentView viewWithTag:100];
        
        
        if (self.seleCardDic) {
            
            if (self.isdining) {
                [subButton setTitle:@"等待商家确认" forState:UIControlStateNormal];
                subButton.enabled = NO;
            }else{
                [subButton setTitle:@"确认支付" forState:UIControlStateNormal];
                subButton.enabled = YES;
                
            }
        }else{
            [subButton setTitle:@"绑定银行卡" forState:UIControlStateNormal];
            subButton.enabled = YES;
        }
        
       
        
        return cell;
    }
    
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.section == 2) {
        if (self.cardArray.count ==0) {
            //去绑定银行卡
            BMSQ_AddBankCardController *vc = [[BMSQ_AddBankCardController alloc]init];
            vc.fromvc =(int)self.navigationController.viewControllers.count;
            [self.navigationController pushViewController:vc animated:YES];
        }else{
            BMSQ_MyBankCardController *vc = [[BMSQ_MyBankCardController alloc]init];
            vc.ispayCard = YES;
            [self.navigationController pushViewController:vc animated:YES];
        }
    }
    
}

#pragma mark 获取验证码

-(void)getIcbcPayValCode:(UIButton *)button{
    
    UIView *superV = [button superview];
    
    while (![superV isKindOfClass:[UITableViewCell class]]) {
        superV = [superV superview];
    }

    UITableViewCell *cell = (UITableViewCell *)superV;
    UITextField *textFiled = (UITextField *)[cell viewWithTag:99];

    
    if(self.seleCardDic == nil){
        UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"没有绑定银行卡" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
        [alterView show];
        return;
    }
    
    
    [SVProgressHUD showWithStatus:@"正在请求"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.consumeCode forKey:@"consumeCode"];
    [params setObject:[self.seleCardDic objectForKey:@"bankAccountCode"] forKey:@"bankAccountCode"];
    [params setObject:textFiled.text.length>0?textFiled.text:@"" forKey:@"mobileNbr"];
    [self initJsonPrcClient:@"2"];
    NSString* vcode = [gloabFunction getSign:@"getIcbcPayValCode" strParams:self.consumeCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getIcbcPayValCode" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        
        if([[responseObject objectForKey:@"code"]integerValue] !=50000){
            
            NSString *message = [NSString stringWithFormat:@"错误 : %@,该号码与银行预留手机号不匹配",[responseObject objectForKey:@"code"]];
            UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:message delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
            [alterView show];
            
        }else{
            [SVProgressHUD showSuccessWithStatus:@"请求成功"];
            [weakSelf timerFireMethod];

            
        }

    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
        [SVProgressHUD showErrorWithStatus:@"请求失败"];
        NSLog(@"请求错误");
        
      
        
        
    }];
    
}


-(void)timerFireMethod{
    
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:0 inSection:1];
    UITableViewCell *cell = [self.m_tableView cellForRowAtIndexPath:indexPath];
    UIButton *button = (UIButton *)[cell.contentView viewWithTag:111];
    
    
    __block int timeoutS = 60;
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_source_t timers = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
    dispatch_source_set_timer(timers, dispatch_walltime(NULL, 0), 1.0 * NSEC_PER_SEC, 0); //每秒执行
    dispatch_source_set_event_handler(timers, ^{
        if (timeoutS <= 0){
            dispatch_source_cancel(timers);
            dispatch_async(dispatch_get_main_queue(), ^{
                [button setTitle:@"再次获取" forState:UIControlStateNormal];
                [button setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
                button.userInteractionEnabled = YES;
            });
        } else {
            NSString *strTime = [NSString stringWithFormat:@"%ds", timeoutS];
            dispatch_async(dispatch_get_main_queue(), ^{
                [button setTitle:strTime forState:UIControlStateNormal];
                [button setTitleColor:[UIColor colorWithHexString:@"0xB2B2B2"] forState:UIControlStateNormal];
                button.userInteractionEnabled = NO;
            });
            timeoutS--;
        }
    });
    dispatch_resume(timers);
}











#pragma mark ---- 请求

/*  购买优惠券 */
-(void)addCouponOrder{

    
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:self.shopCode forKey:@"shopCode"];
    [params setObject:self.batchCouponCode forKey:@"batchCouponCode"];
    [params setObject:self.platBonus forKey:@"platBonus"];
    [params setObject:self.shopBonus forKey:@"shopBonus"];
    [params setObject:self.nbrCoupon forKey:@"couponNbr"];
    
    
    NSString* vcode = [gloabFunction getSign:@"addCouponOrder" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"addCouponOrder" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        if (![[responseObject objectForKey:@"code"] isKindOfClass:[NSNull class]] ) {
            if ([[responseObject objectForKey:@"code"]integerValue] ==50000) {
               
                [SVProgressHUD dismiss];

                wself.orderNbr = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"orderNbr"]];
                wself.consumeCode = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"consumeCode"]];
                wself.payNewPrice = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"realPay"]];
                
                [wself.m_tableView reloadData];

            }else if([[responseObject objectForKey:@"code"]integerValue] ==80240){
                
                NSString *errStr = [NSString stringWithFormat:@"商家还有%@张,可购买",[responseObject objectForKey:@"remaining"]];
                UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:errStr delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
                [alterView show];
                
                [wself.navigationController popViewControllerAnimated:YES];
            }else if([[responseObject objectForKey:@"code"]integerValue] ==80238){
                
                UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"个人购买数量超上限" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
                [alterView show];

                [wself.navigationController popViewControllerAnimated:YES];

            }else{
                
                UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:[NSString stringWithFormat:@"请求返回结果错误[API:addCouponOrder = %@]",[responseObject objectForKey:@"code"]] delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
                [alterView show];
                [wself.navigationController popViewControllerAnimated:YES];

            }
            
        }

    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];

        NSLog(@"请求错误");
    }];
    
    
}
/* 加载银行卡 */
-(void)loadCard{
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [self initJsonPrcClient:@"2"];
    NSString* vcode = [gloabFunction getSign:@"listAllBankCard" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"listAllBankCard" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSArray *array = (NSArray *)responseObject;
        wself.cardArray = array;
        if (wself.cardArray.count>=1) {
            wself.seleCardDic = [wself.cardArray objectAtIndex:0];
        }
        
        [wself.m_tableView reloadData];
        
        wself.isCard = YES;
        [[NSNotificationCenter defaultCenter]postNotificationName:@"removeActiviy" object:nil];

        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"请求错误");
        
        
    }];
    
    
}
/* 得到支付金额*/
-(void)getNewPrice{
    
    
    
    [self initJsonPrcClient:@"2"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:self.shopCode forKey:@"shopCode"];
    [params setObject:self.consumeAmount forKey:@"consumeAmount"];
    [params setObject:self.batchCouponCode.length>0?self.batchCouponCode:@"0" forKey:@"batchCouponCode"];
    [params setObject:self.platBonus forKey:@"platBonus"];
    [params setObject:self.shopBonus forKey:@"shopBonus"];
    [params setObject:self.nbrCoupon forKey:@"nbrCoupon"];
    [params setObject:@"1" forKey:@"payType"];
    [params setObject:self.noDiscountPrice.length>0?self.noDiscountPrice:@"0" forKey:@"noDiscountPrice"];
    
    
    NSString* vcode = [gloabFunction getSign:@"getNewPriceForAndroid" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"getNewPriceForAndroid" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        
        if ([[responseObject objectForKey:@"code"]integerValue] ==50000) {
            wself.payNewPrice = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"newPrice"]];
            [wself.m_tableView reloadData];
            wself.isNewPrice = YES;
            [[NSNotificationCenter defaultCenter]postNotificationName:@"removeActiviy" object:nil];

        }else{
            
            int i = (int)[[responseObject objectForKey:@"code"]integerValue];
            switch (i) {
                case 80236:
                    [SVProgressHUD showErrorWithStatus:@"使用数量超过上限了"];
                    break;
                case 80227:
                    [SVProgressHUD showErrorWithStatus:@"优惠券不可用"];
                    break;
                case 50725:
                    [SVProgressHUD showErrorWithStatus:@"平台红包不够"];
                    break;
                case 50726:
                    [SVProgressHUD showErrorWithStatus:@"商家红包不够"];
                    break;
                case 60515:
                    [SVProgressHUD showErrorWithStatus:@"参与优惠的实际支付金额小于0"];
                    break;
                case 60513:
                    [SVProgressHUD showErrorWithStatus:@"实际支付金额小于最小支付金额"];
                    break;
                default:
                {
                    NSString *str = [NSString stringWithFormat:@"错误 getNewPriceForAndroid＝[%@]",[responseObject objectForKey:@"code"]];
                    [SVProgressHUD showErrorWithStatus:str];
                }
                    break;
            }
         }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"请求错误");
    }];
    
    
}
/* 工行卡订单 */
-(void)bankcardPay{
    [self initJsonPrcClient:@"2"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:self.shopCode forKey:@"shopCode"];
    [params setObject:self.batchCouponCode.length>0?self.batchCouponCode:@"" forKey:@"batchCouponCode"];
    [params setObject:self.platBonus forKey:@"platBonus"];
    [params setObject:self.shopBonus forKey:@"shopBonus"];
    [params setObject:self.nbrCoupon forKey:@"nbrCoupon"];
    [params setObject:self.consumeAmount forKey:@"orderAmount"];  //用户花费金额
    [params setObject:self.noDiscountPrice.length>0?self.noDiscountPrice:@"0" forKey:@"noDiscountPrice"];  //用户花费金额

    
    NSString* vcode = [gloabFunction getSign:@"bankcardPayForAndroid" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"bankcardPayForAndroid" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        if([responseObject objectForKey:@"code"]){
            if (![[responseObject objectForKey:@"code"] isKindOfClass:[NSNull class]] ) {
                if ([[responseObject objectForKey:@"code"]integerValue] ==50000) {
                    wself.orderNbr = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"orderNbr"]];
                    wself.consumeCode = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"consumeCode"]];
                    wself.isCardPay = YES;
                    [[NSNotificationCenter defaultCenter]postNotificationName:@"removeActiviy" object:nil];
                    [wself.m_tableView reloadData];
                }else{
                    NSString *str;
                    if ([[responseObject objectForKey:@"code"]integerValue] ==80227) {
                        //优惠券不可用
                        str = @"优惠券不可用";
                        
                    }else{
                        str = [NSString stringWithFormat:@"错误 getNewPriceForAndroid＝[%@]",[responseObject objectForKey:@"code"]];
                    }
                    
                    UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:str delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定"  , nil];
                    [alterView show];
                    
                    [wself.navigationController popViewControllerAnimated:YES];
                    
                }
              
                
            }else{
                NSString *str = [NSString stringWithFormat:@"错误 getNewPriceForAndroid＝[%@]",[responseObject objectForKey:@"code"]];
                [SVProgressHUD showErrorWithStatus:str];
                
            }
        }else{
            [wself.navigationController popViewControllerAnimated:YES];
        }
     
       
     

    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {

        NSLog(@"请求错误");
    }];
}
/*  外卖订单 */
-(void)pOBankcardPay{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.orderCode forKey:@"orderCode"];
    [params setObject:self.shopCode forKey:@"shopCode"];
    [params setObject:self.batchCouponCode.length>0?self.batchCouponCode:@"" forKey:@"batchCouponCode"];
    [params setObject:self.platBonus forKey:@"platBonus"];
    [params setObject:self.shopBonus forKey:@"shopBonus"];
    [params setObject:self.nbrCoupon forKey:@"nbrCoupon"];
    [params setObject:self.consumeAmount forKey:@"orderAmount"];  //用户花费金额
    [params setObject:self.noDiscountPrice.length>0?self.noDiscountPrice:@"0" forKey:@"noDiscountPrice"];  //不支付金额

    
    NSString* vcode = [gloabFunction getSign:@"pOBankcardPayForAndroid" strParams:self.orderCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"pOBankcardPayForAndroid" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {

        if (![[responseObject objectForKey:@"code"] isKindOfClass:[NSNull class]] ) {
            if ([[responseObject objectForKey:@"code"]integerValue] ==50000) {
                wself.orderNbr = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"orderNbr"]];
                wself.consumeCode = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"consumeCode"]];
                self.isdining = NO;

            }
            
            [wself.m_tableView reloadData];
            wself.isCardPay = YES;
            [[NSNotificationCenter defaultCenter]postNotificationName:@"removeActiviy" object:nil];

        }else{
            
                NSString *str = [NSString stringWithFormat:@"错误 getNewPriceForAndroid＝[%@]",[responseObject objectForKey:@"code"]];
                [SVProgressHUD showErrorWithStatus:str];
                
            
        }
        

        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"请求错误");
    }];
}

#pragma mark --在线支付--


-(void)clickRight{
    [self goPay];
}
-(void)goPay{
    
    if (self.seleCardDic==nil) {

        //去绑定银行卡
        BMSQ_AddBankCardController *vc = [[BMSQ_AddBankCardController alloc]init];
        vc.fromvc =(int)self.navigationController.viewControllers.count;
        [self.navigationController pushViewController:vc animated:YES];

        return;
    }
    
    

    NSIndexPath *indexPath=[NSIndexPath indexPathForRow:1 inSection:1];
    UITableViewCell *cell=[self.m_tableView cellForRowAtIndexPath:indexPath];
    UITextField *yzmText = (UITextField *)[cell.contentView viewWithTag:99];
    
    NSUserDefaults *userD = [NSUserDefaults standardUserDefaults];
    NSString *str = [userD objectForKey:APP_USER_FREEVALCODEPAY];
    
    if([self.payNewPrice floatValue]>300){
        if(yzmText.text.length<=0){
            UIAlertView *alertView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"请输入验证码" delegate:self cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
            
            [alertView show];
            return;
        }
        [self bankcardPayConfirm:yzmText.text];

        
    }else{
        if(![str isEqualToString:@"1"]){
            if(yzmText.text.length<=0){
                UIAlertView *alertView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"请输入验证码" delegate:self cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
                
                [alertView show];
                return;
            }
           
            [self bankcardPayConfirm:yzmText.text];
            
        }else{
            
            [self validatePayPwd];
            
        }
    }
    
}

#pragma mark --- 输入支付验证码
-(void)validatePayPwd{
    
    NSString *price = [NSString stringWithFormat:@"￥%@",self.payNewPrice];
    
    if([self.seleCardDic objectForKey:@"accountNbrLast4"]){
        NSString *accountNbrLast4 = [self.seleCardDic objectForKey:@"accountNbrLast4"];
        NSString *cardMessage = [NSString stringWithFormat:@"工行卡******%@",accountNbrLast4];
        [[[ZCTradeView alloc]init]show:price card:cardMessage];

    }else{
        NSString *cardMessage = @"绑定银行卡";
        [[[ZCTradeView alloc]init]show:price card:cardMessage];
  
    }
    
    
   
}

#pragma mark  支付


-(void)bankcardPayConfirm:(NSString *)valCode{
    
    [SVProgressHUD showWithStatus:@"正在支付" maskType:SVProgressHUDMaskTypeClear];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];

    [params setObject: self.consumeCode forKey:@"consumeCode"];
    [params setObject:[self.seleCardDic objectForKey:@"bankAccountCode"] forKey:@"bankAccountCode"];
    [params setObject:valCode forKey:@"valCode"];
    
    [self initJsonPrcClient:@"2"];
    NSString* vcode = [gloabFunction getSign:@"bankcardPayConfirm" strParams:self.consumeCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"bankcardPayConfirm" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        RRC_webViewController *vc = [[RRC_webViewController alloc]init];
        vc.fromVC = wself.fromVC;
        vc.isPayResult = YES;
        vc.isTakeOut = wself.isTakeout;
        vc.isBuy = wself.isBuy;
        if ([[responseObject objectForKey:@"code"]integerValue] == 50000) {
            [SVProgressHUD dismiss];
            vc.requestUrl =  [NSString stringWithFormat:@"%@Browser/paySucc?consumeCode=%@",H5_URL,self.consumeCode];
            vc.navtitle = @"支付结果";
            vc.isHidenNav = NO;
            [wself.navigationController pushViewController:vc animated:YES];
        }else{
            
            if([[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]] isEqualToString:@"B2621"]){
                NSString *retmsg = [responseObject objectForKey:@"retmsg"];
                [SVProgressHUD showErrorWithStatus:retmsg];
            }
            else{
                vc.requestUrl =[NSString stringWithFormat:@"%@Browser/payFail?errCode=%@",H5_URL,[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]]];
                vc.navtitle = @"支付结果";
                vc.isHidenNav = NO;
                [wself.navigationController pushViewController:vc animated:YES];
            }
        }
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD showErrorWithStatus:@"请求数据失败！！"];
    }];
}



-(BOOL)textFieldShouldBeginEditing:(UITextField *)textField{
    
    [self.m_tableView setContentOffset:CGPointMake(0, 120)];
    
    return YES;
}
-(BOOL)textFieldShouldReturn:(UITextField *)textField{
    
    //    if (textField.tag == 99) {
    //        [self.m_tableView setContentOffset:CGPointMake(0, 0)];
    [textField resignFirstResponder];
    //
    //    }
    return YES;
}

- (void)goBack{
    
    if(self.isdining){
        
        [self.navigationController popToViewController:[self.navigationController.viewControllers objectAtIndex:self.fromVC-1] animated:YES];

    }else{
        
        
        UIAlertView *alertView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"确认要放弃支付吗？" delegate:self cancelButtonTitle:@"确定" otherButtonTitles:@"取消", nil];
        alertView.tag = 1000;
        alertView.delegate =self;
        [alertView show];
    }
    
}
#pragma mark 放弃支付
-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if (alertView.tag ==1000) {
        if (buttonIndex == 0) {
            if (self.isTakeout) {
                [self pOCancelBankcardPay];
            }else{
                [self cancelBankcardPay];
            }
        }
    }
}
-(void)cancelBankcardPay{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.consumeCode forKey:@"consumeCode"];
    NSString* vcode = [gloabFunction getSign:@"cancelBankcardPay" strParams:self.isTakeout?self.orderNbr:self.consumeCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak __typeof(self)weakSelf = self;
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"cancelBankcardPay" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [weakSelf.navigationController popToViewController:[weakSelf.navigationController.viewControllers objectAtIndex:weakSelf.fromVC-1] animated:YES];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [weakSelf.navigationController popToViewController:[weakSelf.navigationController.viewControllers objectAtIndex:weakSelf.fromVC-1] animated:YES];
        
    }];
}
-(void)pOCancelBankcardPay{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.consumeCode forKey:@"consumeCode"];
    NSString* vcode = [gloabFunction getSign:@"pOCancelBankcardPay" strParams:self.isTakeout?self.orderNbr:self.consumeCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak __typeof(self)weakSelf = self;
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"pOCancelBankcardPay" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [weakSelf.navigationController popToViewController:[weakSelf.navigationController.viewControllers objectAtIndex:weakSelf.fromVC-1] animated:YES];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [weakSelf.navigationController popToViewController:[weakSelf.navigationController.viewControllers objectAtIndex:weakSelf.fromVC-1] animated:YES];
        
    }];
}






-(void)changeText:(UITextField *)textField{
    
    if (textField.tag == 99) {
        if (textField.text.length >8) {
            textField.text = [textField.text substringToIndex:8];
        }
        
    }
    
}
 
-(void)addCard:(NSNotification *)noti{
    [self loadCard];
}

#pragma mark  验证支付码 支付
-(void)verification:(NSNotification *)notification{
    NSArray *array = notification.object;
    NSString *nums = [array componentsJoinedByString:@""];
    
    [SVProgressHUD showWithStatus:@"正在支付" maskType:SVProgressHUDMaskTypeClear];
     [self initJsonPrcClient:@"2"];
     NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
     
     NSString *payPwd = [MD5 MD5Value:nums];
     
     [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
     [params setObject:payPwd forKey:@"payPwd"];
     
     NSString* vcode = [gloabFunction getSign:@"validatePayPwd" strParams:[gloabFunction getUserCode]];
     [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
     [params setObject:vcode forKey:@"vcode"];
     [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
     
     __weak typeof(self) weakSelf =  self;
     [self.jsonPrcClient invokeMethod:@"validatePayPwd" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
     
     int resut = [[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]]intValue];
     
     if (resut == 1) {
         [weakSelf pay];
     
     }else{
         [SVProgressHUD showErrorWithStatus:@"验证码不正确"];
         
         
         [[NSNotificationCenter defaultCenter]postNotificationName:@"removeAllNum" object:nil];;
     }
     
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
     [SVProgressHUD dismiss];
     
     }];
     
    
}
-(void)pay{
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject: self.consumeCode forKey:@"consumeCode"];
    [params setObject:[self.seleCardDic objectForKey:@"bankAccountCode"] forKey:@"bankAccountCode"];
    [params setObject:@"" forKey:@"valCode"];
    
    [self initJsonPrcClient:@"2"];
    NSString* vcode = [gloabFunction getSign:@"bankcardPayConfirm" strParams:self.consumeCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"bankcardPayConfirm" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [wself hiddenZCTradeView];
        RRC_webViewController *vc = [[RRC_webViewController alloc]init];
        vc.fromVC = wself.fromVC;
        vc.isPayResult = YES;
        vc.isTakeOut = wself.isTakeout;
        if ([[responseObject objectForKey:@"code"]integerValue] == 50000) {
            [SVProgressHUD dismiss];
            vc.requestUrl =  [NSString stringWithFormat:@"%@Browser/paySucc?consumeCode=%@",H5_URL,self.consumeCode];
            vc.navtitle = @"支付结果";
            vc.isHidenNav = NO;
            [wself.navigationController pushViewController:vc animated:YES];
        }else{
            
            if([[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]] isEqualToString:@"B2621"]){
                NSString *retmsg = [responseObject objectForKey:@"retmsg"];
                [SVProgressHUD showErrorWithStatus:retmsg];
            }
            else{
                vc.requestUrl =[NSString stringWithFormat:@"%@Browser/payFail?errCode=%@",H5_URL,[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]]];
                vc.navtitle = @"支付结果";
                vc.isHidenNav = NO;
                [wself.navigationController pushViewController:vc animated:YES];
            }
        }
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD showErrorWithStatus:@"请求数据失败！！"];
    }];
}


-(void)hiddenZCTradeView{

    [[NSNotificationCenter defaultCenter] postNotificationName:@"ZCTradeInputViewOkButtonClick" object:self userInfo:nil];
    
}

@end
