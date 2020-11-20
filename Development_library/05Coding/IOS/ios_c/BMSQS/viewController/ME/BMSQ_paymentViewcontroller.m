//
//  BMSQ_paymentViewcontroller.m
//  BMSQC
//
//  Created by liuqin on 16/1/13.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_paymentViewcontroller.h"

#import "BMSQ_MyBankCardController.h"
#import "MobClick.h"
#import "CreatQRView.h"

@interface BMSQ_paymentViewcontroller ()<UIAlertViewDelegate,UIWebViewDelegate,CreatQRViewDelegate>

@property (nonatomic, strong)UILabel *seleCardLabel;

@property (nonatomic, strong)NSDictionary *cardDic;
@property (nonatomic, strong)NSString *timeStr;

@property (nonatomic, strong)CreatQRView *qrView;

@property (nonatomic, assign)BOOL isCard; //Yes 工行卡请求成功
@property (nonatomic, assign)BOOL isTime; //Yes 时间请求成功


@end




@implementation BMSQ_paymentViewcontroller


- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"payment"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"payment"];
}


-(void)viewDidLoad{
    [super viewDidLoad];
    
    [self setNavBackItem];
    [self setNavigationBar];
    [self setNavTitle:@"付款"];
    
    /* 用户没有绑定银行卡的时候进入些页面 需要 */
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(loadCard) name:@"loadMyCard" object:nil];
    
    /* 用户多张银行卡的时候进入些页面 选择需要 */
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(seleCard:) name:@"seleCard" object:nil];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(removeSVP) name:@"removeSVP" object:nil];
    [SVProgressHUD showWithStatus:@""];

    
    [self loadCard];

    
}
-(void)havedCard{
    
    float w = APP_VIEW_WIDTH-60;
    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(10, w+APP_VIEW_ORIGIN_Y+40+30, APP_VIEW_WIDTH-20, 30)];
    label.textColor = APP_TEXTCOLOR;
    label.text = @"默认付款方式";
    label.font = [UIFont systemFontOfSize:14];
    [self.view addSubview:label];
    
    
    self.seleCardLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, label.frame.origin.y+label.frame.size.height, APP_VIEW_WIDTH, 40)];
    self.seleCardLabel.backgroundColor = [UIColor whiteColor];
    self.seleCardLabel.textColor = APP_TEXTCOLOR;
    [self.view addSubview:self.seleCardLabel];
    
    self.seleCardLabel.userInteractionEnabled = YES;
    UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(clickSeleCard)];
    [self.seleCardLabel addGestureRecognizer:tapGesture];
    self.seleCardLabel.text = [NSString stringWithFormat:@"  工行卡* * * * * *%@",[self.cardDic objectForKey:@"accountNbrLast4"]];
    UIImageView *imageView = [[UIImageView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-50, self.seleCardLabel.frame.origin.y+(40-15)/2, 10, 15)];
    [imageView setImage:[UIImage imageNamed:@"garyright"]];
    [self.view addSubview:imageView];
    
    self.qrView = [[CreatQRView alloc]initWithFrame:CGRectMake(30, APP_VIEW_ORIGIN_Y+40, w, w)];
    self.qrView.delegate = self;
    self.qrView.imageHead = self.headImage;
    [self.view addSubview:self.qrView];
    
    [self getTimeSever];
    [self startTimeDown];
}
-(void)noCard{
    
  NSString *str = @"您还没开通工商银行惠支付,暂时";
   NSString *str1 = @"无法使用扫码支付功能~";
  CGSize  size = [str boundingRectWithSize:CGSizeMake(MAXFLOAT,MAXFLOAT)
                                                options:NSStringDrawingUsesLineFragmentOrigin
                                             attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:14]}
                                                context:nil].size;
    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake((APP_VIEW_WIDTH-size.width)/2, APP_VIEW_HEIGHT/2-50, size.width, size.height)];
    label.text = str;
    label.textColor = APP_TEXTCOLOR;
    label.backgroundColor = [UIColor clearColor];
    label.font =[UIFont systemFontOfSize:14];
    [self.view addSubview:label];
    
    UILabel *label1 = [[UILabel alloc]initWithFrame:CGRectMake(label.frame.origin.x, label.frame.origin.y+label.frame.size.height+5, size.width, size.height)];
    label1.text = str1;
    label1.textColor = APP_TEXTCOLOR;
    label1.backgroundColor = [UIColor clearColor];
    label1.font =[UIFont systemFontOfSize:14];
    [self.view addSubview:label1];
    
    UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(label.frame.origin.x, label1.frame.origin.y+label1.frame.size.height+5, label1.frame.size.width, 30)];
    button.layer.masksToBounds = YES;
    button.layer.cornerRadius = 4;
    [button setTitle:@"开通惠支付" forState:UIControlStateNormal];
    [button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    button.titleLabel.font = [UIFont systemFontOfSize:14];
    button.backgroundColor = APP_NAVCOLOR;
    [self.view addSubview:button];
    [button addTarget:self action:@selector(gotoOpenCard) forControlEvents:UIControlEventTouchUpInside];
    
    
}
#pragma mark --倒计时--
- (void)startTimeDown
{
    
    __block int timeout = 60;
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_source_t _timer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
    dispatch_source_set_timer(_timer, dispatch_walltime(NULL, 0), 1.0 * NSEC_PER_SEC, 0); //每秒执行
    dispatch_source_set_event_handler(_timer, ^{
        if (timeout <= 0){
            dispatch_source_cancel(_timer);
            dispatch_async(dispatch_get_main_queue(), ^{
//                [self.timeBtn setTitle:@"再次获取" forState:UIControlStateNormal];
//                [self.timeBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
//                self.timeBtn.userInteractionEnabled = YES;
                [self getTimeSever];
                [self startTimeDown];
            });
        } else {
//            NSString *strTime = [NSString stringWithFormat:@"%d秒", timeout];
            dispatch_async(dispatch_get_main_queue(), ^{
//                [self.timeBtn setTitle:strTime forState:UIControlStateNormal];
//                [self.timeBtn setTitleColor:RGB(121, 121, 121) forState:UIControlStateNormal];
//                self.timeBtn.userInteractionEnabled = NO;
            });
            timeout--;
        }
    });
    dispatch_resume(_timer);
    
}
#pragma mark --得到时间--
-(void)getTimeSever{
    
    NSString *str = [NSString stringWithFormat:@"%@Index/index",H5_URL];
    NSURL *url = [NSURL URLWithString:str];
    NSURLRequest *request=[NSURLRequest requestWithURL:url];
    NSURLConnection *connection=[[NSURLConnection alloc]initWithRequest:request
                                                               delegate:self
                                                       startImmediately:YES];
    [connection start];
    

}
- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{
    if ([response respondsToSelector:@selector(allHeaderFields)]) {
        
        NSHTTPURLResponse *httpResponse=(NSHTTPURLResponse *)response;
        if ([response respondsToSelector:@selector(allHeaderFields)]) {
            
            NSDictionary *dic=[httpResponse allHeaderFields];
            NSString *time=[dic objectForKey:@"Date"];
            time = [time substringWithRange:NSMakeRange(5, 20)];
            //设置源时间字符串的格式
            NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
            [formatter setDateFormat:@"dd MMM yyyy HH:mm:ss"];
            
            NSTimeZone *timeZone = [NSTimeZone timeZoneWithName:@"GMT"];
            [formatter setTimeZone:timeZone];
            //需要配置区域，不然会造成模拟器正常，真机日期为null的情况
            NSLocale *local=[[NSLocale alloc]initWithLocaleIdentifier:@"en_US_POSIX"];
            [formatter setLocale:local];
            
            NSDate* date = [formatter dateFromString:time];
            //时间戳
            NSTimeInterval a = [date timeIntervalSince1970];
            NSString *timeString = [NSString stringWithFormat:@"%.0f", a];
            int timeInt = [timeString intValue];
            
            
             NSString *str3 =  [NSMutableString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%1x",timeInt]];
            
            
            NSMutableString *qrString = [[NSMutableString alloc]init];
            if ([str3 length]<10) {//长度小于10位，
                
                NSInteger index=10-str3.length;
                NSString *str=[self GetRandomLetterWithNum:index];
                for (NSInteger i=0; i<str.length; i++) {
                    NSInteger pos=arc4random() % [str3 length]+1;
                    str3=[NSString stringWithFormat:@"%@%@%@",[str3 substringToIndex:pos],[str substringWithRange:NSMakeRange(i, 1)],[str3 substringWithRange:NSMakeRange(pos, [str3 length]-pos)]];
                }
                
                [qrString appendFormat:@"%@",str3];
            }
            else
            {
                [qrString appendFormat:@"%@",str3];
            }
            
          self.isTime = YES;
          self.timeStr = qrString;
        
        [[NSNotificationCenter defaultCenter]postNotificationName:@"removeSVP" object:nil];

            
        }

    }else{
        
        [SVProgressHUD showErrorWithStatus:@"时间错误"];
    }
}

-(void)removeSVP{
    
    if (self.isTime && self.isCard) {
        [SVProgressHUD dismiss];
        [self getQR];
    }
}

#pragma mark -- 生成二维码--
-(void)getQR{
    [self.qrView creatCode:[self getQRCodeString]];
}
-(void)refreshQR{
    [self.qrView creatCode:[self getQRCodeString]];
}
#pragma mark -- 加载银行卡 --
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
        if (array.count>0) {
            wself.isCard = YES;
            wself.cardDic = [array objectAtIndex:0];
            [[NSNotificationCenter defaultCenter]postNotificationName:@"removeSVP" object:nil];
            [wself havedCard ];


        }else{
//            UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"您还没有绑定工商银行卡" delegate:self cancelButtonTitle:@"返回" otherButtonTitles:@"绑定银行卡", nil];
//            alterView.tag = 100;
//            [alterView show];
            [SVProgressHUD dismiss];
            [wself noCard];
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        NSLog(@"请求错误");
        
    }];
    
    
}
#pragma mark -- 组装生成二进制字符串 --
- (NSString *)getQRCodeString{
    
    NSMutableString  *qrString=[NSMutableString stringWithFormat:@"payType:%@",[gloabFunction getUserCode]];
    // (1)bankCardCode前六位转化为16进制
    NSString *accountNbrPre6 = [NSString stringWithFormat:@"%@",[self.cardDic objectForKey:@"accountNbrPre6"]];
    int a=[accountNbrPre6 intValue];
    [qrString appendString:[NSString stringWithFormat:@"%1x",a]];
    
    //(2)后4 （转化为16进制，不足四位g-z大小写都可以，直接补后面）
    NSString *accountNbrLast4 = [NSString stringWithFormat:@"%@",[self.cardDic objectForKey:@"accountNbrLast4"]];
    int b=[accountNbrLast4 intValue];
    NSString *str2= [[NSString alloc] initWithFormat:@"%1x",b];

    if ([str2 length]<4) {//长度小于四位，
        //需要补的位数
        NSInteger index=4-str2.length;
        [qrString appendFormat:@"%@",str2];
        [qrString appendString:[self GetRandomLetterWithNum:index]];
        
    }else{
        [qrString appendFormat:@"%@",str2];

    }
    
    [qrString appendString:self.timeStr];
 
//    //(3)时间戳转化为16进制,不足10位随机插入g-z的字母
//    NSString *str3= [[NSString alloc] initWithFormat:@"%1x",[self.timeStr intValue]];
//    
//    if ([str3 length]<10) {//长度小于10位，
//        
//        NSInteger index=10-str3.length;
//        NSString *str=[self GetRandomLetterWithNum:index];
//        for (NSInteger i=0; i<str.length; i++) {
//            NSInteger pos=arc4random() % [str3 length]+1;
//             str3=[NSString stringWithFormat:@"%@%@%@",[str3 substringToIndex:pos],[str substringWithRange:NSMakeRange(i, 1)],[str3 substringWithRange:NSMakeRange(pos, [str3 length]-pos)]];
//        }
//        
//        [qrString appendFormat:@"%@",str3];
//    }
//    else
//    {
//        [qrString appendFormat:@"%@",str3];
//    }
    
    return qrString;
}
- (NSString *)GetRandomLetterWithNum:(NSInteger)num{
    
    NSString *lettrString=@"ghijklmnopqrstuvwxyz";
    
    NSMutableString *mut=[NSMutableString stringWithString:@""];
    
    for (NSInteger i=0; i<num; i++) {
        NSInteger index=arc4random() % 20;
        [mut appendString:[lettrString substringWithRange:NSMakeRange(index, 1)]];
    }
    
    return mut;
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if (alertView.tag == 100) {
        if (buttonIndex ==0) {
            [self.navigationController popViewControllerAnimated:YES];
        }
        else if (buttonIndex ==1){
          
        }
    }
    
}

#pragma mark -- 选择银行卡 --
-(void)clickSeleCard{
    BMSQ_MyBankCardController *vc = [[BMSQ_MyBankCardController alloc]init];
    vc.isPayQR = YES;
    vc.isCanDele = YES;
    [self.navigationController pushViewController:vc animated:YES];
}
-(void)seleCard:(NSNotification *)noti{
    
    self.cardDic =noti.object;
    self.seleCardLabel.text = [NSString stringWithFormat:@"  工行卡* * * * * *%@",[self.cardDic objectForKey:@"accountNbrLast4"]];
    [self getQR];

}


-(void)gotoOpenCard{
    BMSQ_MyBankCardController *vc = [[BMSQ_MyBankCardController alloc]init];
    vc.isPayQR = YES;
    vc.isCanDele =YES;
    [self.navigationController pushViewController:vc animated:YES];
    
}
@end
