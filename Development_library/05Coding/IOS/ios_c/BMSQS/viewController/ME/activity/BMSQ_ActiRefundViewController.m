//
//  BMSQ_ActiRefundViewController.m
//  BMSQC
//
//  Created by liuqin on 16/1/19.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_ActiRefundViewController.h"

#import "RefundView.h"
#import "ActiVityModel.h"
#import "MobClick.h"

//@interface ActiView : UIView
//
//@property(nonatomic, strong)UILabel *leftView;
//@property(nonatomic, strong)UILabel *rightView;
//
//
//
//@end

@implementation ActiView

-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        
        
        self.backgroundColor = [UIColor whiteColor];
        
        self.leftView = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH/2, frame.size.height)];
        self.leftView.backgroundColor = [UIColor clearColor];
        self.leftView.textColor = APP_TEXTCOLOR;
        self.leftView.textAlignment = NSTextAlignmentLeft;
        self.leftView.font = [UIFont systemFontOfSize:12.f];
        [self addSubview:self.leftView];
        
        self.rightView = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2+10, 0, APP_VIEW_WIDTH/2-20, frame.size.height)];
        self.rightView.backgroundColor = [UIColor clearColor];
        self.rightView.textAlignment = NSTextAlignmentRight;
        self.rightView.textColor = APP_TEXTCOLOR;
        self.rightView.font = [UIFont systemFontOfSize:12.f];
        [self addSubview:self.rightView];
        

    }
    return self;
}

@end


@interface BMSQ_ActiRefundViewController ()<RefundViewDelegate,UITextViewDelegate>

@property (nonatomic, strong)UILabel *orderTypeLabel;//订单状态

@property (nonatomic, strong)UILabel *refundTypeLabel;//退款状态
@property (nonatomic, strong)UILabel *refundMessageLabel;//退款说明
@property (nonatomic, strong)UILabel *refundNbrLabel;//退款编号
@property (nonatomic, strong)UILabel *mobileLabel;//电话投诉
@property (nonatomic, strong)UIButton *moneyButton;//金额按扭

@property (nonatomic, strong)UIView *firView; //第一部分
@property (nonatomic, strong)UIView *sevView; //第二部分
@property (nonatomic, strong)UIView *thrView; //第三部分
@property (nonatomic, strong)UIView *fourView;

@property (nonatomic, strong)UIScrollView *mainSC;


@property (nonatomic, strong)NSDictionary *resultDic;

@property (nonatomic, strong)NSMutableArray *sltReason;

@property (nonatomic, strong)UIView *sltReasonView;
@property (nonatomic, strong)UITextView *othertextView;
@property (nonatomic, strong)UITextView *planttextView;



@property (nonatomic,assign)float contextOffY;


@property (nonatomic, strong)UIView *messageView;


@end




@implementation BMSQ_ActiRefundViewController

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"ActiRefund"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"ActiRefund"];
}


-(void)viewDidLoad{
    [super viewDidLoad];
    [self setNavBackItem];
    [self setNavTitle:@"申请退款"];
    
    self.contextOffY =0.0;
    
    self.mainSC = [[UIScrollView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.mainSC.backgroundColor = [UIColor clearColor];
    [self.view addSubview:self.mainSC];
    
    self.sltReason = [[NSMutableArray alloc]init];
    
    [self getUserActCodeInfo];
}

-(void)initView{
    /* 订单类型 */
    self.firView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
    self.firView.backgroundColor = [UIColor whiteColor];
    [self.mainSC addSubview:self.firView];
    
    self.orderTypeLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, self.firView.frame.size.height)];
    self.orderTypeLabel.text = @"订单类型:活动预订";
    self.orderTypeLabel.textColor = APP_TEXTCOLOR;
    self.orderTypeLabel.font = [UIFont systemFontOfSize:14];
    [self.firView addSubview:self.orderTypeLabel];
    
    [self initSecView:self.firView];
    
}

-(void)initSecView:(UIView *)v{
    /* 退款状态 */
    
    
    
    
    self.sevView = [[UIView alloc]initWithFrame:CGRectMake(0, v.frame.size.height, APP_VIEW_WIDTH, 70)];
    self.sevView.backgroundColor = [UIColor clearColor];
    [self.mainSC addSubview:self.sevView];
    
    
    UIImageView *imageStatus = [[UIImageView alloc]initWithFrame:CGRectMake(15, 5, 20, 20)];
    [imageStatus setImage:[UIImage imageNamed:@"refund_status"]];
    [self.sevView addSubview:imageStatus];
    
    
    
    self.refundTypeLabel = [[UILabel alloc]initWithFrame:CGRectMake(50, 0, APP_VIEW_WIDTH-60, 30)];
    
    NSString *str = [ActiVityModel getStatusStr:[NSString stringWithFormat:@"%@",[self.resultDic objectForKey:@"status"]]];

    self.refundTypeLabel.text = [NSString stringWithFormat:@"退款状态:%@",str];
    self.refundTypeLabel.textColor = APP_TEXTCOLOR;
    self.refundTypeLabel.font = [UIFont systemFontOfSize:14];
    [self.sevView addSubview:self.refundTypeLabel];
    [self.resultDic objectForKey:@""];
    self.refundMessageLabel = [[UILabel alloc]initWithFrame:CGRectMake(50,25, APP_VIEW_WIDTH-60, 15)];
    self.refundMessageLabel.text =[NSString stringWithFormat:@"退款说明:%@",[self.resultDic objectForKey:@"refundExplain"]];// @"退款说明:3-10个工作日内原路退回";
    self.refundMessageLabel.textColor = APP_TEXTCOLOR;
    self.refundMessageLabel.font = [UIFont systemFontOfSize:12];
    [self.sevView addSubview:self.refundMessageLabel];
    
    self.refundNbrLabel = [[UILabel alloc]initWithFrame:CGRectMake(50,40, APP_VIEW_WIDTH-60, 15)];
    self.refundNbrLabel.text = [NSString stringWithFormat:@"订单编号:%@",[self.resultDic objectForKey:@"orderNbr"]];//@"订单编号:000000000000000000000";
    self.refundNbrLabel.textColor = APP_TEXTCOLOR;
    self.refundNbrLabel.font = [UIFont systemFontOfSize:12];
    [self.sevView addSubview:self.refundNbrLabel];
    
    
    self.mobileLabel = [[UILabel alloc]initWithFrame:CGRectMake(50,55, APP_VIEW_WIDTH-60, 15)];
    self.mobileLabel.text =[NSString stringWithFormat:@"电话投诉:%@",[self.resultDic objectForKey:@"hotLine"]];// @"电话投诉:400-04-95588";
    self.mobileLabel.textColor = APP_TEXTCOLOR;
    self.mobileLabel.font = [UIFont systemFontOfSize:12];
    [self.sevView addSubview:self.mobileLabel];

    [self initThrView:self.sevView];
    
}
-(void)initThrView:(UIView *)v{
    
    self.thrView =[[UIView alloc]initWithFrame:CGRectMake(0, v.frame.size.height+v.frame.origin.y+10, APP_VIEW_WIDTH, 40)];
    self.thrView.backgroundColor = [UIColor whiteColor];
    [self.mainSC addSubview:self.thrView];
    
    
    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(10,0, 100, 40)];
    label.text = @"退款金额";
    label.font = [UIFont systemFontOfSize:14.f];
    [self.thrView addSubview:label];
    
    self.moneyButton = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-110, 0, 100, 40)];
    NSString *toRefundAmount = [NSString stringWithFormat:@"%@",[self.resultDic objectForKey:@"toRefundAmount"]];
    [self.moneyButton setTitle:[NSString stringWithFormat:@"%0.2f元",[toRefundAmount floatValue]] forState:UIControlStateNormal];
    [self.moneyButton setImage:[UIImage imageNamed:@"garydown"] forState:UIControlStateNormal];
    [self.moneyButton setImage:[UIImage imageNamed:@"garyup"] forState:UIControlStateSelected];

    [self.moneyButton setImageEdgeInsets:UIEdgeInsetsMake(0, 90, 0, 0)];
    self.moneyButton.backgroundColor = [UIColor clearColor];
    [self.moneyButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [self.moneyButton addTarget:self action:@selector(clickMonty:) forControlEvents:UIControlEventTouchUpInside];
    
    [self.thrView addSubview:self.moneyButton];
    [self initFourView:self.thrView];
    
    
    self.messageView = [[UIView alloc]initWithFrame:CGRectMake(0, self.moneyButton.frame.origin.y+self.moneyButton.frame.size.height, APP_VIEW_WIDTH, 0)];
    self.messageView.backgroundColor = [UIColor clearColor];
    [self.thrView insertSubview:self.messageView belowSubview:self.moneyButton];
    self.messageView.hidden = YES;

    
    
    UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 0.15)];
    lineView.backgroundColor = APP_TEXTCOLOR;
    [self.messageView addSubview:lineView];
    
    for (int i = 0; i<3; i++) {
        ActiView *acView = [[ActiView alloc]initWithFrame:CGRectMake(0, 0.15+i*25, APP_VIEW_WIDTH, 25)];
        if (i==0) {
            acView.leftView.text = @"工行卡";
            acView.rightView.text = [NSString stringWithFormat:@"%@元",[self.resultDic objectForKey:@"bankcardRefund"]];
        }else if (i==1){
            acView.leftView.text = @"惠圈红包";
            acView.rightView.text = [NSString stringWithFormat:@"%@元",[self.resultDic objectForKey:@"platBonusRefund"]];

        }else if (i==2){
            acView.leftView.text = @"商家红包";
            acView.rightView.text = [NSString stringWithFormat:@"%@元",[self.resultDic objectForKey:@"shopBonusRefund"]];

        }
        
        [self.messageView addSubview:acView];
    }
    
    
    
    
    
    
    
    
    

}

-(void)clickMonty:(UIButton *)btn{
    btn.selected = !btn.selected;
    
    if (btn.selected) {
        [UIView animateWithDuration:0.2 animations:^{
            self.messageView.hidden = NO;

            
            self.messageView.frame =CGRectMake(0, self.moneyButton.frame.origin.y+self.moneyButton.frame.size.height, APP_VIEW_WIDTH, 100);
            CGRect rect = self.sltReasonView.frame;
            rect.origin.y = self.thrView.frame.origin.y+self.thrView.frame.size.height+self.messageView.frame.size.height;
            self.sltReasonView.frame = rect;
            
            
            rect = self.fourView.frame;
            rect.origin.y = self.sltReasonView.frame.origin.y+self.sltReasonView.frame.size.height+10;
            self.fourView.frame = rect;


            
        }];
    }else{
        [UIView animateWithDuration:0.2 animations:^{
            self.messageView.frame =CGRectMake(0, self.moneyButton.frame.origin.y+self.moneyButton.frame.size.height, APP_VIEW_WIDTH, 0);
            
            CGRect rect = self.sltReasonView.frame;
            rect.origin.y = self.thrView.frame.origin.y+self.thrView.frame.size.height+10;
            self.sltReasonView.frame = rect;

            rect = self.fourView.frame;
            rect.origin.y = self.sltReasonView.frame.origin.y+self.sltReasonView.frame.size.height+10;
            self.fourView.frame = rect;
            
            self.messageView.hidden = YES;
            

        }];
    }
    
}
-(void)initFourView:(UIView *)v{
    
    NSArray *array =[self.resultDic objectForKey:@"sltReason"];

    
    
    self.sltReasonView = [[UIView alloc]initWithFrame:CGRectMake(0, v.frame.origin.y+v.frame.size.height+10,APP_VIEW_WIDTH, 40 +41*array.count+150)];
    self.sltReasonView.backgroundColor = [UIColor whiteColor];
    [self.mainSC addSubview:self.sltReasonView];
    
    UILabel *titleLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 40)];
    titleLabel.text = @"退款金额(至少选一项)";
    titleLabel.textColor = APP_TEXTCOLOR;
    titleLabel.backgroundColor = [UIColor whiteColor];
    titleLabel.font = [UIFont systemFontOfSize:14.f];
    [self.sltReasonView addSubview:titleLabel];

    UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(10, 40, APP_VIEW_WIDTH-20, 0.5)];
    lineView.backgroundColor = self.view.backgroundColor;
    [self.sltReasonView addSubview:lineView];
    
    
    for(int i=0;i<array.count;i++){
        RefundView *refundView = [[RefundView alloc]initWithFrame:CGRectMake(0,45 +41*i, APP_VIEW_WIDTH,25)];
        NSDictionary *dic = [array objectAtIndex:i];
        refundView.delegate = self;
        [refundView setData:dic];
        [self.sltReasonView addSubview:refundView];
    }
    
    
    
    self.othertextView = [[UITextView alloc]initWithFrame:CGRectMake(10, 40 +41*array.count+10, APP_VIEW_WIDTH-20, 60)];
    self.othertextView.layer.borderColor = [self.view.backgroundColor CGColor];
    self.othertextView.layer.borderWidth = 0.5;
    self.othertextView.delegate = self;
    self.othertextView.returnKeyType = UIReturnKeyDone;
    [self.sltReasonView addSubview:self.othertextView];

    
    self.contextOffY = self.othertextView.frame.origin.y+self.sltReasonView.frame.origin.y;
    
    
    
    UIButton *submitBtn = [[UIButton alloc]initWithFrame:CGRectMake((APP_VIEW_WIDTH-120)/2,self.othertextView.frame.origin.y+self.othertextView.frame.size.height+10 , 120, 30)];
    [submitBtn setTitle:@"确认" forState:UIControlStateNormal];
    [submitBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    submitBtn.backgroundColor = APP_NAVCOLOR;
    submitBtn.titleLabel.font = [UIFont systemFontOfSize:15];
    submitBtn.layer.cornerRadius = 4;
    submitBtn.layer.masksToBounds = YES;
    [submitBtn addTarget:self action:@selector(clickSubmit) forControlEvents:UIControlEventTouchUpInside];
    [self.sltReasonView addSubview:submitBtn];
    
        self.mainSC.contentSize = CGSizeMake(APP_VIEW_WIDTH, self.sltReasonView.frame.origin.y+self.sltReasonView.frame.size.height+20);
    
//    [self initMessageView:self.sltReasonView];
}
-(void)initMessageView:(UIView *)v{
    
    self.fourView = [[UIView alloc]initWithFrame:CGRectMake(0, v.frame.origin.y+v.frame.size.height+10,APP_VIEW_WIDTH, 140)];
    self.fourView.backgroundColor = [UIColor whiteColor];
    [self.mainSC addSubview:self.fourView];
    

    

    
    self.mainSC.contentSize = CGSizeMake(APP_VIEW_WIDTH, self.fourView.frame.origin.y+self.fourView.frame.size.height+120);
    
    
 
}

#pragma  mark 退款原因
-(void)getUserActCodeInfo{
    
    [self initJsonPrcClient:@"2"];
    [SVProgressHUD showWithStatus:@""];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.actCode forKey:@"actCode"];
    
    
    NSString* vcode = [gloabFunction getSign:@"getUserActCodeInfo" strParams:self.actCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getUserActCodeInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        weakSelf.resultDic = responseObject;
        [weakSelf.sltReason addObjectsFromArray:[self.resultDic objectForKey:@"sltReason"]];

        [weakSelf initView];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
    }];
    
    
}
-(void)seleResult:(int)i{
    
    for (int j=0; j< self.sltReason.count; j++) {
        NSMutableDictionary *dic = [[NSMutableDictionary alloc]initWithDictionary:[self.sltReason objectAtIndex:j]];
        int seleInt = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"id"]]intValue];
        if (seleInt == i) {
            [dic setObject:@"1" forKey:@"selected"];
            [self.sltReason removeObjectAtIndex:j];
            [self.sltReason insertObject:dic atIndex:j];
        }else{
            [dic setObject:@"0" forKey:@"selected"];
            [self.sltReason removeObjectAtIndex:j];
            [self.sltReason insertObject:dic atIndex:j];
        }
    }
    
    for (UIView *vi in self.sltReasonView.subviews) {
        if([vi isKindOfClass:[RefundView class]]){
            [vi removeFromSuperview];
        }
    }
    
    for(int i=0;i<self.sltReason.count;i++){
        RefundView *refundView = [[RefundView alloc]initWithFrame:CGRectMake(0,45 +41*i, APP_VIEW_WIDTH,25)];
        NSDictionary *dic = [self.sltReason objectAtIndex:i];
        refundView.delegate = self;
        [refundView setData:dic];
        [self.sltReasonView addSubview:refundView];
    }
}

-(void)textViewDidBeginEditing:(UITextView *)textView{
    
    if (textView.tag == 100) {
        self.mainSC.contentOffset = CGPointMake(0, self.contextOffY+30);

    }else{
        
        if(APP_VIEW_HEIGHT!=480){
            self.mainSC.contentOffset = CGPointMake(0, 220);

        }else{
            self.mainSC.contentOffset = CGPointMake(0, 280);

        }

    }
    
    
}

#pragma mark - UITextView Delegate Methods
-(BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text
{
    if ([text isEqualToString:@"\n"]) {
        [textView resignFirstResponder];
        self.mainSC.contentOffset = CGPointMake(0, 0);

        return NO;
    }
    return YES;
}

-(void)clickSubmit{
    

    NSString *refundReason;
    for (int i=0; i<self.sltReason.count; i++) {
        NSDictionary *dic = [self.sltReason objectAtIndex:i];
        NSString *selected = [NSString stringWithFormat:@"%@",[dic objectForKey:@"selected"]];
        if ([selected intValue]==1) {
            refundReason = [NSString stringWithFormat:@"%@",[dic objectForKey:@"id"]];
        }
    }
    
    [self initJsonPrcClient:@"2"];
    [SVProgressHUD showWithStatus:@""];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[NSString stringWithFormat:@"%@",[self.resultDic objectForKey:@"orderCode"]] forKey:@"orderCode"];
    [params setObject:[NSString stringWithFormat:@"%@",[self.resultDic objectForKey:@"actCode"]] forKey:@"actCode"];
    [params setObject:[NSString stringWithFormat:@"%@",[self.resultDic objectForKey:@"bankcardRefund"]] forKey:@"bankcardRefund"];
    [params setObject:[NSString stringWithFormat:@"%@",[self.resultDic objectForKey:@"platBonusRefund"]] forKey:@"platBonusRefund"];
    [params setObject:[NSString stringWithFormat:@"%@",[self.resultDic objectForKey:@"shopBonusRefund"]] forKey:@"shopBonusRefund"];
    [params setObject:self.othertextView.text.length>0?self.othertextView.text:@"" forKey:@"refundRemark"];
    [params setObject:refundReason.length>0?refundReason:@"" forKey:@"refundReason"];

    
    NSString* vcode = [gloabFunction getSign:@"actCodeApplyRefund" strParams:[NSString stringWithFormat:@"%@",[self.resultDic objectForKey:@"orderCode"]]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [self.jsonPrcClient invokeMethod:@"actCodeApplyRefund" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSString *code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if ([code intValue] == 50000) {
            [SVProgressHUD showSuccessWithStatus:@"申请退款成功"];
        }else if ([code intValue] == 52200){
            [SVProgressHUD showErrorWithStatus:@"不能够退款"];

        }else if ([code intValue] == 52201){
            [SVProgressHUD showErrorWithStatus:@"购买当日不能退款"];

        }else if ([code intValue] == 55000){
            [SVProgressHUD showErrorWithStatus:@"当日订单不能部分退款"];
            
        }else{
           ;
            [SVProgressHUD showErrorWithStatus:[NSString stringWithFormat:@"申请退款失败[code=%@]",code]];
        }
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
    }];

    
}
@end
