//
//  BMSQ_PayDetailSViewController.m
//  BMSQC
//
//  Created by liuqin on 15/8/31.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_PayDetailSViewController.h"
#import "BMSQ_OrderViewController.h"

#import "SVProgressHUD.h"
#import "UITextFieldEx.h"

#import "BMSQ_PayCardViewController.h"
#import "BMSQ_couponListViewController.h"


#define SIZEFONE 13

@interface BMSQ_PayDetailSViewController ()<UITableViewDataSource,UITableViewDelegate,UITextFieldDelegate,TextFieldDelegate,UIAlertViewDelegate>

@property(nonatomic, strong)UITableView *my_tableView;

@property (nonatomic, assign)BOOL haveData;  //有无数据

@property(nonatomic, strong)NSString *userCouponCode;
@property(nonatomic, strong)NSDictionary *resultDic;
@property(nonatomic, strong)NSMutableArray *bounsArray;


@property (nonatomic, assign)BOOL isCard;  //是否银行卡支付
@property(nonatomic, strong)NSMutableArray *conpoutypeArray;
@property(nonatomic, strong)NSString *minRealPay; //至少支付金额

//@property(nonatomic, strong)NSString *UserPayMeony;//用户花费的钱数   .h有
@property(nonatomic, strong)NSString *conpoutype;


@property(nonatomic, strong)NSString *disMoney; //优惠金额
@property(nonatomic, strong)NSString *shopBouns; //用户用商家红包钱数
@property(nonatomic, strong)NSString *plantBouns;//用户用惠圈红包钱数

@property(nonatomic, strong)NSString *UserReallyPay; //用户实际支付钱数
@property(nonatomic, strong)NSString *mealFirstDec;  //首单减多少

@property(nonatomic, strong)NSString *cardDiscount;   //工行卡折扣
@property(nonatomic, strong)NSString *memberCardDisCount;   //会员卡折扣

//抵扣券张数
@property(nonatomic,assign)int nbr;
@property(nonatomic,assign)float count;//满 30
@property(nonatomic,assign)float dis;//减 20
@property(nonatomic,assign)int limitedNbr;//减 20

@property(nonatomic,assign)int useLimitedNbr;//减 20


@property(nonatomic,assign)int userCount; //用户拥有优惠券张数



@property (nonatomic, assign)float  minReduction;
@property (nonatomic, assign)float  minDiscount;


////////////////
@property (nonatomic, assign)NSInteger section;
@property (nonatomic, assign)NSInteger row;

@property (nonatomic, strong)NSMutableDictionary *seleCoupon;


@property (nonatomic, assign)BOOL B_isInputDisCount;   //商家输入有没有优惠金额
@property (nonatomic, strong)NSString *B_inputDisCount;   //商家输入有没有优惠金额




@end

@implementation BMSQ_PayDetailSViewController



-(void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    [SVProgressHUD dismiss];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"买单"];
    
    
 
    
    UIButton* btnback = [UIButton buttonWithType:UIButtonTypeCustom];
    btnback.frame = CGRectMake(APP_VIEW_WIDTH - 64, 20, 64, 44);
    [btnback setTitle:@"下一步" forState:0];
    btnback.titleLabel.font = [UIFont boldSystemFontOfSize:15];
    [btnback addTarget:self action:@selector(clickRight:) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:btnback];
    

    
    self.nbr = 0;
    
    self.bounsArray = [[NSMutableArray alloc]initWithArray:@[@"0",@"0",@"0",@"0"]];//红包
    self.seleCoupon = [[NSMutableDictionary alloc]init];
    
    
    if(self.batchCouponCode.length>0){
        self.isCard = NO;
    }else{
        self.isCard = YES;
        self.conpoutypeArray = [[NSMutableArray alloc]init];
    }
    
    
    
    
    self.my_tableView =[[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT) style:UITableViewStyleGrouped];
    self.my_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.my_tableView.delegate = self;
    self.my_tableView.dataSource = self;
    self.my_tableView.backgroundColor = [UIColor clearColor];
//    [self.view addSubview:self.my_tableView];
    [self.view insertSubview:self.my_tableView belowSubview:self.navigationView];
    
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(refreshSeleCoupon:) name:@"refreshSeleCoupon" object:nil];
    
      [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(keyboardHiddend) name:UIKeyboardWillHideNotification object:nil];

    
    [self listUserPayInfo];
}
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return self.haveData?8:0;
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.section ==0) {
        static NSString *identifier = @"payCell0";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
            bgView.backgroundColor = UICOLOR(251, 254, 228, 1); //[UIColor whiteColor];
            [cell addSubview:bgView];
            
            UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 5, 60, 30)];
            leftLabel.backgroundColor = [UIColor clearColor];
            leftLabel.font = [UIFont systemFontOfSize:SIZEFONE];
            leftLabel.layer.cornerRadius = 5;
            leftLabel.layer.masksToBounds = YES;
            leftLabel.layer.borderWidth = 0.7;
            leftLabel.textAlignment = NSTextAlignmentCenter;
            leftLabel.text =@"满就送";
            leftLabel.textColor = [UIColor blackColor];
            leftLabel.layer.borderColor = [[UIColor grayColor]CGColor];
            [bgView addSubview:leftLabel];
            
            
            UILabel *rightLabel = [[UILabel alloc]init];
            rightLabel.backgroundColor = [UIColor clearColor];
            rightLabel.font = [UIFont systemFontOfSize:SIZEFONE];
            rightLabel.numberOfLines = 0;
            rightLabel.textColor = UICOLOR(202, 157, 101, 1);
            rightLabel.tag = 100;
            [bgView addSubview:rightLabel];
        }
        NSString *sendCoupon =[NSString stringWithFormat:@"%@",[self.resultDic objectForKey:@"sendCoupon"]];
        CGSize size= [sendCoupon boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-80-10, MAXFLOAT)
                                              options:NSStringDrawingUsesLineFragmentOrigin
                                           attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:SIZEFONE]}
                                              context:nil].size;
        
        
        UILabel *riginLabel = (UILabel *)[cell viewWithTag:100];
        riginLabel.text = sendCoupon;
        riginLabel.frame = CGRectMake(80, 0, APP_VIEW_WIDTH-80-10, size.height>40?size.height:40);
        return cell;

    }
    else if (indexPath.section ==1){
        static NSString *identifier = @"payCell1";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;

            UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 5, APP_VIEW_WIDTH, 40)];
            bgView.backgroundColor = [UIColor whiteColor];
            [cell addSubview:bgView];
            
            
            
            UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 5, 60, 30)];
            leftLabel.backgroundColor = [UIColor whiteColor];
            leftLabel.font = [UIFont systemFontOfSize:SIZEFONE];
             leftLabel.text =@"商家名称";
            leftLabel.textColor = [UIColor blackColor];
            [bgView addSubview:leftLabel];
            
            
            UILabel *rightLabel = [[UILabel alloc]initWithFrame:CGRectMake(80, 5, APP_VIEW_WIDTH-90, 30)];
            rightLabel.backgroundColor = [UIColor whiteColor];
            rightLabel.font = [UIFont systemFontOfSize:SIZEFONE];
            rightLabel.textAlignment = NSTextAlignmentRight;
            rightLabel.text = self.shopName;
            [bgView addSubview:rightLabel];
        }
        return cell;
        
    }
    else if (indexPath.section ==2){
        if (indexPath.row ==0) {
            static NSString *identifier = @"payCell2";
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
            if (cell == nil) {
                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
                cell.backgroundColor = [UIColor clearColor];
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                
                UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 5, APP_VIEW_WIDTH, 40)];
                bgView.backgroundColor = [UIColor whiteColor];
                [cell addSubview:bgView];
                
                
                
                UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 5, 60, 30)];
                leftLabel.backgroundColor = [UIColor whiteColor];
                leftLabel.font = [UIFont systemFontOfSize:SIZEFONE];
                leftLabel.text =@"消费金额";
                leftLabel.textColor = [UIColor blackColor];
                [bgView addSubview:leftLabel];
                
                
                UILabel *rightLabel = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-30, 5, 30, 30)];
                rightLabel.backgroundColor = [UIColor whiteColor];
                rightLabel.font = [UIFont systemFontOfSize:SIZEFONE];
                rightLabel.textAlignment = NSTextAlignmentCenter;
                rightLabel.text = @"元";
                rightLabel.textColor = UICOLOR(182, 0, 12, 1.0);
                [bgView addSubview:rightLabel];
                
                
                UITextField *textField = [[UITextField alloc]initWithFrame:CGRectMake(80, 5, APP_VIEW_WIDTH-80-30, 30)];
                textField.textAlignment = NSTextAlignmentRight;
                textField.keyboardType = UIKeyboardTypeDecimalPad;
                textField.backgroundColor = [UIColor clearColor];
                textField.placeholder = @"请输入金额";
                textField.tag = 50000;
                textField.textColor = UICOLOR(182, 0, 12, 1.0);
                
                textField.delegate = self;
                textField.keyboardType = UIKeyboardTypeDecimalPad;
                [textField addTarget:self action:@selector(chnageTextFild:) forControlEvents:UIControlEventEditingChanged];
                textField.font = [UIFont systemFontOfSize:SIZEFONE];
                [bgView addSubview:textField];
                
                
                
            }
            
            UITextField *consumAmountText = (UITextField *)[cell viewWithTag:50000];
            consumAmountText.text = [self.consumAmount floatValue ]>0?self.consumAmount:@"";
            return cell;

        }else{
            static NSString *identifier = @"payCell2";
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
            if (cell == nil) {
                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
                cell.backgroundColor = [UIColor whiteColor];
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
             
                
                UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(15,5, 15, 15)];
                [button setImage:[UIImage imageNamed:@"radio_no"] forState:UIControlStateNormal];
                [button setImage:[UIImage imageNamed:@"radio_yes"] forState:UIControlStateSelected];
                button.tag = 100;
                [cell addSubview:button];

                UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(50, 0, APP_VIEW_WIDTH-60, 25)];
                label.text = @"输入不参与优惠金额(如酒水、海鲜)";
                label.font = [UIFont systemFontOfSize:11];
                label.textColor = APP_TEXTCOLOR;
                [cell addSubview:label];
                
                
                UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 25, APP_VIEW_WIDTH, 40)];
                bgView.backgroundColor = [UIColor whiteColor];
                bgView.tag = 200;
                [cell addSubview:bgView];
                bgView.hidden = YES;

                
                UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 5, 100, 30)];
                leftLabel.backgroundColor = [UIColor whiteColor];
                leftLabel.font = [UIFont systemFontOfSize:SIZEFONE];
                leftLabel.text =@"不参与优惠金额";
                leftLabel.textColor = [UIColor blackColor];
                [bgView addSubview:leftLabel];
                
                
                UILabel *rightLabel = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-30, 5, 30, 30)];
                rightLabel.backgroundColor = [UIColor whiteColor];
                rightLabel.font = [UIFont systemFontOfSize:SIZEFONE];
                rightLabel.textAlignment = NSTextAlignmentCenter;
                rightLabel.text = @"元";
                rightLabel.textColor = UICOLOR(182, 0, 12, 1.0);
                [bgView addSubview:rightLabel];
                
                
                UITextField *textField = [[UITextField alloc]initWithFrame:CGRectMake(120, 5, APP_VIEW_WIDTH-120-30, 30)];
                textField.textAlignment = NSTextAlignmentRight;
                textField.keyboardType = UIKeyboardTypeDecimalPad;
                textField.backgroundColor = [UIColor clearColor];
                textField.placeholder = @"请输入金额";
                textField.tag = 80000;
                textField.textColor = UICOLOR(182, 0, 12, 1.0);
                
                textField.delegate = self;
                textField.keyboardType = UIKeyboardTypeDecimalPad;
                [textField addTarget:self action:@selector(chnageTextFild:) forControlEvents:UIControlEventEditingChanged];
                textField.font = [UIFont systemFontOfSize:SIZEFONE];
                [bgView addSubview:textField];
                

                
            }
            UIButton *button = (UIButton *)[cell viewWithTag:100];
            UIView *bgview = (UIView *)[cell viewWithTag:200];

            button.selected = self.B_isInputDisCount;
            if (button.selected) {
                bgview.hidden = NO;
            }else{
                bgview.hidden = YES;

            }
            UITextField *consumAmountText = (UITextField *)[cell viewWithTag:80000];
            consumAmountText.text = [self.B_inputDisCount floatValue ]>0?self.B_inputDisCount:@"";
            return cell;
        }
        
    }  //输入金额
    else if (indexPath.section ==3){
        static NSString *identifier = @"payCell3";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            
            UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 5, APP_VIEW_WIDTH, 40)];
            bgView.backgroundColor = [UIColor whiteColor];
            [cell addSubview:bgView];
            
            UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 5, 60, 30)];
            leftLabel.backgroundColor = [UIColor whiteColor];
            leftLabel.font = [UIFont systemFontOfSize:SIZEFONE];
            leftLabel.tag = 100;
            leftLabel.textColor = UICOLOR(202, 157, 101, 1);
            [bgView addSubview:leftLabel];
            
            
            UILabel *couponLabel = [[UILabel alloc]initWithFrame:CGRectMake(65, 5, 130, 30)];
            couponLabel.backgroundColor = [UIColor whiteColor];
            couponLabel.font = [UIFont systemFontOfSize:SIZEFONE];
            couponLabel.tag = 200;
            couponLabel.textColor = UICOLOR(202, 157, 101, 1);

            [bgView addSubview:couponLabel];
            
            UIView *type3View = [[UIView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-100, 0, 90, 40)];
            type3View.backgroundColor = [UIColor clearColor];
            type3View.tag = 300;
            [bgView addSubview:type3View];
            
            UIButton *minusButton = [[UIButton alloc]initWithFrame:CGRectMake(0, 2, 18, 18)];
            [minusButton setImage:[UIImage imageNamed:@"order_minus"] forState:UIControlStateNormal];
            [minusButton addTarget:self action:@selector(addNbr:) forControlEvents:UIControlEventTouchUpInside];
            minusButton.tag = 77;
            [type3View addSubview:minusButton];

            UIButton *addButton = [[UIButton alloc]initWithFrame:CGRectMake(type3View.frame.size.width-20, 2, 18, 18)];
            [addButton setImage:[UIImage imageNamed:@"order_add"] forState:UIControlStateNormal];
            [addButton addTarget:self action:@selector(addNbr:) forControlEvents:UIControlEventTouchUpInside];
            addButton.tag = 88;
            [type3View addSubview:addButton];
            
            UILabel *countLabel = [[UILabel alloc]initWithFrame:CGRectMake(20, 2, addButton.frame.origin.x-25, 15)];
            countLabel.backgroundColor = [UIColor clearColor];
            countLabel.font = [UIFont systemFontOfSize:SIZEFONE];
            countLabel.text = @"0";
            countLabel.tag = 999;
            countLabel.textColor = UICOLOR(202, 157, 101, 1);
            countLabel.textAlignment = NSTextAlignmentCenter;
            [type3View addSubview:countLabel];
            
            UILabel *desLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 20, type3View.frame.size.width, 20)];
            desLabel.backgroundColor = [UIColor clearColor];
            desLabel.font = [UIFont systemFontOfSize:SIZEFONE-3];
            desLabel.textAlignment = NSTextAlignmentCenter;
            desLabel.text = @"减免10元";
            desLabel.textColor = UICOLOR(202, 157, 101, 1);
            desLabel.tag = 888;
            [type3View addSubview:desLabel];
            type3View.hidden = YES;
            
            UILabel *type4Label = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-100, 0, 90, 40)];
            type4Label.backgroundColor = [UIColor clearColor];
            type4Label.font = [UIFont systemFontOfSize:SIZEFONE];
            type4Label.tag = 400;
            type4Label.text = @"减免0元";
            type4Label.textColor = UICOLOR(202, 157, 101, 1);
            type4Label.textAlignment = NSTextAlignmentRight;
            [bgView addSubview:type4Label];
            type4Label.hidden = YES;

            
            UIImageView *riginImage = [[UIImageView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH - 30, 15, 8, 10)];
            riginImage.tag = 500;
            [riginImage setImage:[UIImage imageNamed:@"garyright"]];
            [bgView addSubview:riginImage];
            riginImage.hidden = YES;
            
            
        }
        UILabel *typeLabel = (UILabel *)[cell viewWithTag:100];
        UILabel *couponLabel = (UILabel *)[cell viewWithTag:200];
        UIView *type3View = (UIView *)[cell viewWithTag:300];
        UILabel *type4Label = (UILabel *)[cell viewWithTag:400];
        UILabel *nbrlabel = (UILabel *)[cell viewWithTag:999];
        UILabel *desLabel = (UILabel *)[cell viewWithTag:888];
        UIImageView *rightImage = (UIImageView *)[cell viewWithTag:500];

        if(!self.isCard){
            rightImage.hidden =YES;
            NSDictionary *coupon = [self.resultDic objectForKey:@"coupon"];
            NSDictionary *couponInfo = [coupon objectForKey:@"couponInfo"];
//            if (couponInfo.count>0) {
            NSString *couponType = [NSString stringWithFormat:@"%@",[couponInfo objectForKey:@"couponType"]];
            if ([couponType intValue] == 3){
                typeLabel.text = @"抵扣券";
                float availablePrice = [[NSString stringWithFormat:@"%@",[couponInfo objectForKey:@"availablePrice"]]floatValue];
                float insteadPrice = [[NSString stringWithFormat:@"%@",[couponInfo objectForKey:@"insteadPrice"]]floatValue];

                couponLabel.text = [NSString stringWithFormat:@"满%.2f减%0.1f",availablePrice,insteadPrice];
                type3View.hidden = NO;
                type4Label.hidden =YES;
                nbrlabel.text = [NSString stringWithFormat:@"%d",self.nbr];
                desLabel.text = [NSString stringWithFormat:@"减免%0.2f元",self.dis*self.nbr];
                
                self.disMoney = [NSString stringWithFormat:@"%0.2f",self.dis*self.nbr];
//            }
                
            }else{
                typeLabel.text = @"折扣券";
                float availablePrice = [[NSString stringWithFormat:@"%@",[couponInfo objectForKey:@"availablePrice"]]floatValue];
                float discountPercent = [[NSString stringWithFormat:@"%@",[couponInfo objectForKey:@"discountPercent"]]floatValue];

                
                couponLabel.text = [NSString stringWithFormat:@"满%0.2f折%0.1f",availablePrice,discountPercent];
                type3View.hidden = YES;
                type4Label.hidden =NO;
                NSString *discountPercent1 = [NSString stringWithFormat:@"%@",[couponInfo objectForKey:@"discountPercent"]];
//                NSString *availablePrice = [NSString stringWithFormat:@"%@",[couponInfo objectForKey:@"availablePrice"]];

                if ([self.consumAmount floatValue] >= availablePrice) {
                    float result = self.consumAmount.floatValue -(self.consumAmount.floatValue*[discountPercent1 floatValue]/10);
                    type4Label.text =  [NSString stringWithFormat:@"减免%0.2f元",result ];
                    self.disMoney = [NSString stringWithFormat:@"%0.2f",result];
                }else{
                     type4Label.text =  [NSString stringWithFormat:@"减免0元" ];
                }
              
                
                
            }
            
        }
        else{
            rightImage.hidden =NO;

            NSString *discount = [self.conpoutypeArray objectAtIndex:0];
            NSString *reduction = [self.conpoutypeArray objectAtIndex:1];
            if ([discount isEqualToString:@"1"] && [reduction isEqualToString:@"1"]) {
                
                if (indexPath.row == 0) {
                    typeLabel.text = @"抵扣券";
                    
                    
                    if ([self.seleCoupon objectForKey:@"batchCouponCode"] ) {
                        
                        if ([[self.seleCoupon objectForKey:@"couponType"]isEqualToString:@"3"]) {
                            
                            rightImage.hidden =YES;
                            couponLabel.text = [NSString stringWithFormat:@"满%@减%@",[self.seleCoupon objectForKey:@"availablePrice"],[self.seleCoupon objectForKey:@"insteadPrice"]];
                            type3View.hidden = NO;
                            type4Label.hidden =YES;
                            couponLabel.hidden = NO;

                            nbrlabel.text = [NSString stringWithFormat:@"%d",self.nbr];
                            desLabel.text = [NSString stringWithFormat:@"减免%0.2f元",self.dis*self.nbr];
                            self.disMoney = [NSString stringWithFormat:@"%0.2f",self.dis*self.nbr];
                            
                        }else{
                            rightImage.hidden =NO;
                            type3View.hidden = YES;
                            type4Label.hidden =YES;
                            couponLabel.hidden = YES;

                        }
                  
                        
                        
                    }

                }else{
                    typeLabel.text = @"折扣券";
                    
                    if ([self.seleCoupon objectForKey:@"batchCouponCode"]) {
                        
                        if ([[self.seleCoupon objectForKey:@"couponType"]isEqualToString:@"4"]) {
                            
                            rightImage.hidden =YES;
                            couponLabel.hidden = NO;

                            
                              NSString *discountPercent = [NSString stringWithFormat:@"%@",[self.seleCoupon objectForKey:@"discountPercent"]];
                            couponLabel.text = [NSString stringWithFormat:@"满%@折%0.2f",[self.seleCoupon objectForKey:@"availablePrice"],[discountPercent floatValue]];
                            NSString *availablePrice = [NSString stringWithFormat:@"%@",[self.seleCoupon objectForKey:@"availablePrice"]];
                            type3View.hidden = YES;
                            type4Label.hidden =NO;
                            if ([self.consumAmount floatValue]>= [availablePrice floatValue]) {
                                float result = self.consumAmount.floatValue -(self.consumAmount.floatValue*[discountPercent floatValue]/10);
                                type4Label.text =  [NSString stringWithFormat:@"减免%0.2f元",result ];
                                self.disMoney = [NSString stringWithFormat:@"%0.2f",result];

                            }else{
                            type4Label.text =  [NSString stringWithFormat:@"减免0元" ];
                                self.disMoney = @"0";
                                self.batchCouponCode = @"";
                            }
                            
                        }else{
                            rightImage.hidden =NO;
                            type3View.hidden = YES;
                            type4Label.hidden =YES;
                            couponLabel.hidden = YES;
                        }
                        
                      

                    }
                    
                }
                
            }else{
                
                if ([discount isEqualToString:@"1"]) {
                    typeLabel.text = @"折扣券";
                    
                    if ([self.seleCoupon objectForKey:@"batchCouponCode"]) {
                        
                        if ([[self.seleCoupon objectForKey:@"couponType"]isEqualToString:@"4"]) {
                            
                            rightImage.hidden =YES;
                            couponLabel.text = [NSString stringWithFormat:@"满%@折%@",[self.seleCoupon objectForKey:@"availablePrice"],[self.seleCoupon objectForKey:@"discountPercent"]];
                            type3View.hidden = YES;
                            type4Label.hidden =NO;
                            NSString *discountPercent = [NSString stringWithFormat:@"%@",[self.seleCoupon objectForKey:@"discountPercent"]];
                            float result = self.consumAmount.floatValue -(self.consumAmount.floatValue*[discountPercent floatValue]/10);
                            type4Label.text =  [NSString stringWithFormat:@"减免%0.2f元",result ];
                            self.disMoney = [NSString stringWithFormat:@"%0.2f",result];
                            
                        }
                     
                        
                    }
                    

                }else{
                    typeLabel.text = @"抵扣券";
                    
                    
                    if ([self.seleCoupon objectForKey:@"batchCouponCode"]) {
                        if ([[self.seleCoupon objectForKey:@"couponType"]isEqualToString:@"3"]) {
                            
                            rightImage.hidden =YES;
                            couponLabel.text = [NSString stringWithFormat:@"满%@减%@",[self.seleCoupon objectForKey:@"availablePrice"],[self.seleCoupon objectForKey:@"insteadPrice"]];
                            type3View.hidden = NO;
                            type4Label.hidden =YES;
                            nbrlabel.text = [NSString stringWithFormat:@"%d",self.nbr];
                            desLabel.text = [NSString stringWithFormat:@"减免%0.2f元",self.dis*self.nbr];
                            self.disMoney = [NSString stringWithFormat:@"%0.2f",self.dis*self.nbr];
                            
                        }
                    }
                    

                }
                
                
                
            }
            
            
            
        }
        return cell;
        
    }
    else if (indexPath.section ==4){
        static NSString *identifier = @"payCell1";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;

            UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 5, APP_VIEW_WIDTH, 0)];
            bgView.backgroundColor = [UIColor clearColor];
            bgView.tag = 100;
            [cell addSubview:bgView];

        }
        
        
        UIView *bgview =(UIView *)[cell viewWithTag:100];


        int h = 0;
        
        for (int i=0;i<self.bounsArray.count;i++) {
            int value = [[self.bounsArray objectAtIndex:i]intValue];
            NSDictionary *bonus = [self.resultDic objectForKey:@"bonus"];

                if (i ==0 ) {
                    if (value ==1) {
                        UIView *labelView = [[UIView alloc]initWithFrame:CGRectMake(0, h*41, APP_VIEW_WIDTH, 40)];
                        labelView.backgroundColor = [UIColor whiteColor];
                        [bgview addSubview:labelView];
                        UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 70, 40)];
                        leftLabel.backgroundColor = [UIColor whiteColor];
                        leftLabel.font = [UIFont systemFontOfSize:SIZEFONE];
                        leftLabel.textColor = [UIColor blackColor];
                        leftLabel.layer.borderColor = [[UIColor grayColor]CGColor];
                        [labelView addSubview:leftLabel];
                        leftLabel.text = @"惠圈红包";
                        
                        UILabel *rightLabel = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-190, 0, 180, 40)];
                        rightLabel.backgroundColor = [UIColor whiteColor];
                        rightLabel.font = [UIFont systemFontOfSize:SIZEFONE];
                        rightLabel.textColor = [UIColor blackColor];
                        rightLabel.textAlignment = NSTextAlignmentRight;
                        rightLabel.layer.borderColor = [[UIColor grayColor]CGColor];
                        [labelView addSubview:rightLabel];
                        rightLabel.text = [NSString stringWithFormat:@"元,惠圈红包可用%@元",[bonus objectForKey:@"platBonus"]] ;
                        
                        UITextField *inputText = [[UITextField alloc]initWithFrame:CGRectMake(leftLabel.frame.origin.x+leftLabel.frame.size.width, 0, rightLabel.frame.origin.x-(leftLabel.frame.origin.x+leftLabel.frame.size.width), 40)];
                        inputText.backgroundColor = [UIColor clearColor];
                        inputText.textAlignment = NSTextAlignmentCenter;
                        inputText.font = [UIFont systemFontOfSize:SIZEFONE];
                        inputText.tag = 60000;
                        inputText.placeholder = @"输入金额";
                        inputText.keyboardType = UIKeyboardTypeDecimalPad;
                        inputText.textColor = UICOLOR(182, 0, 12, 1.0);
                        inputText.returnKeyType = UIReturnKeyDone;
                        inputText.delegate = self;
                        [inputText addTarget:self action:@selector(chnageTextFild:) forControlEvents:UIControlEventEditingChanged];
                        [labelView addSubview:inputText];

                        
                        
                        h=h+1;
                    }
                
                }else if (i ==1 ){
                    if (value ==1) {
                        
                        UIView *labelView = [[UIView alloc]initWithFrame:CGRectMake(0, h*41, APP_VIEW_WIDTH, 40)];
                        [bgview addSubview:labelView];
                        labelView.backgroundColor = [UIColor whiteColor];
                        UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 70, 40)];
                        leftLabel.backgroundColor = [UIColor whiteColor];
                        leftLabel.font = [UIFont systemFontOfSize:SIZEFONE];
                        leftLabel.textColor = [UIColor blackColor];
                        leftLabel.layer.borderColor = [[UIColor grayColor]CGColor];
                        [labelView addSubview:leftLabel];
                        leftLabel.text = @"商家红包";
                        
                        UILabel *rightLabel = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-190, 0, 180, 40)];
                        rightLabel.backgroundColor = [UIColor whiteColor];
                        rightLabel.font = [UIFont systemFontOfSize:SIZEFONE];
                        rightLabel.textColor = [UIColor blackColor];
                        rightLabel.textAlignment = NSTextAlignmentRight;
                        rightLabel.layer.borderColor = [[UIColor grayColor]CGColor];
                        [labelView addSubview:rightLabel];
                        rightLabel.text = [NSString stringWithFormat:@"元,商家红包可用%@元",[bonus objectForKey:@"shopBonus"]] ;
                        
                        UITextField *inputText = [[UITextField alloc]initWithFrame:CGRectMake(leftLabel.frame.origin.x+leftLabel.frame.size.width, 0, rightLabel.frame.origin.x-(leftLabel.frame.origin.x+leftLabel.frame.size.width), 40)];
                        inputText.backgroundColor = [UIColor clearColor];
                        inputText.textAlignment = NSTextAlignmentCenter;
                        inputText.font = [UIFont systemFontOfSize:SIZEFONE];
                        inputText.placeholder = @"输入金额";
                        inputText.delegate = self;
                        inputText.tag = 70000;
                        inputText.textColor = UICOLOR(182, 0, 12, 1.0);

                        inputText.keyboardType = UIKeyboardTypeDecimalPad;
                        [inputText addTarget:self action:@selector(chnageTextFild:) forControlEvents:UIControlEventEditingChanged];
                        [labelView addSubview:inputText];
                        h=h+1;

                    }
                   

                }else if (i==2 ){
                    
                    if (value ==1) {
                        NSDictionary *icbc = [self.resultDic objectForKey:@"card"];

                        UIView *labelView = [[UIView alloc]initWithFrame:CGRectMake(0, h*41, APP_VIEW_WIDTH, 40)];
                        [bgview addSubview:labelView];
                        labelView.backgroundColor = [UIColor whiteColor];
                        UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 70, 40)];
                        leftLabel.backgroundColor = [UIColor whiteColor];
                        leftLabel.font = [UIFont systemFontOfSize:SIZEFONE];
                        leftLabel.textColor = [UIColor blackColor];
                        leftLabel.layer.borderColor = [[UIColor grayColor]CGColor];
                        [labelView addSubview:leftLabel];
                        leftLabel.text = @"会员卡折扣";
                        
                        UILabel *rightLabel = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-190, 0, 180, 40)];
                        rightLabel.backgroundColor = [UIColor whiteColor];
                        rightLabel.font = [UIFont systemFontOfSize:SIZEFONE];
                        rightLabel.textColor = [UIColor blackColor];
                        rightLabel.textAlignment = NSTextAlignmentRight;
                        rightLabel.layer.borderColor = [[UIColor grayColor]CGColor];
                        [labelView addSubview:rightLabel];
                        rightLabel.text = [NSString stringWithFormat:@"%@折",[icbc objectForKey:@"discount"]] ;
                        h=h+1;
                        
                    }

                }else if (i==3 ){
                    if (value ==1) {
                       
                        NSDictionary *card = [self.resultDic objectForKey:@"icbc"];

                        UIView *labelView = [[UIView alloc]initWithFrame:CGRectMake(0, h*41, APP_VIEW_WIDTH, 40)];
                        labelView.backgroundColor = [UIColor whiteColor];
                        [bgview addSubview:labelView];
                        
                        UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 70, 40)];
                        leftLabel.backgroundColor = [UIColor whiteColor];
                        leftLabel.font = [UIFont systemFontOfSize:SIZEFONE];
                        leftLabel.textColor = [UIColor blackColor];
                        leftLabel.layer.borderColor = [[UIColor grayColor]CGColor];
                        [labelView addSubview:leftLabel];
                        leftLabel.text = @"工行卡折扣";
                        
                        UILabel *rightLabel = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-190, 0, 180, 40)];
                        rightLabel.backgroundColor = [UIColor whiteColor];
                        rightLabel.font = [UIFont systemFontOfSize:SIZEFONE];
                        rightLabel.textColor = [UIColor blackColor];
                        rightLabel.textAlignment = NSTextAlignmentRight;
                        rightLabel.layer.borderColor = [[UIColor grayColor]CGColor];
                        [labelView addSubview:rightLabel];
                        rightLabel.textColor = UICOLOR(182, 0, 12, 1.0);

                        NSString *onlinePaymentDiscount = [NSString stringWithFormat:@"%@",[card objectForKey:@"onlinePaymentDiscount"]];
                        
                        rightLabel.text = [NSString stringWithFormat:@"%0.1f折",[onlinePaymentDiscount floatValue]] ;
                                                           
                        h=h+1;

                        
                    }
                    
                   

                }
                
                
           
   
        }
        
        
      bgview.frame = CGRectMake(0, 5, APP_VIEW_WIDTH, h*41);

        
        return cell;
        
    }
    else if (indexPath.section ==5){
        static NSString *identifier = @"payCell5";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;

            UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 1, APP_VIEW_WIDTH, 30)];
            bgView.backgroundColor = [UIColor clearColor];
            [cell addSubview:bgView];
            
            
            
            UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 30)];
            leftLabel.backgroundColor = [UIColor whiteColor];
            leftLabel.font = [UIFont systemFontOfSize:SIZEFONE];
            leftLabel.text =@"*首单立减100元";
            leftLabel.textAlignment = NSTextAlignmentRight;
            leftLabel.textColor = UICOLOR(202, 157, 101, 1);
            leftLabel.tag = 100;
            leftLabel.textColor = [UIColor blackColor];
            [bgView addSubview:leftLabel];
         }
        UILabel *leftLable = (UILabel *)[cell viewWithTag:100];
        leftLable.text=[NSString stringWithFormat:@"*首单立减%@元",self.mealFirstDec];
        
        
        return cell;
        
    }  //首单立减
    else if (indexPath.section ==6){
        static NSString *identifier = @"payCell6";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;

            UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 5, APP_VIEW_WIDTH, 40)];
            bgView.backgroundColor = [UIColor whiteColor];
            [cell addSubview:bgView];
            
            
            
            UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 5, 60, 30)];
            leftLabel.backgroundColor = [UIColor whiteColor];
            leftLabel.font = [UIFont systemFontOfSize:SIZEFONE];
            leftLabel.text =@"实际支付";
            leftLabel.textColor = [UIColor blackColor];
            [bgView addSubview:leftLabel];
            
            
            UILabel *rightLabel = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-30, 5, 30, 30)];
            rightLabel.backgroundColor = [UIColor whiteColor];
            rightLabel.font = [UIFont systemFontOfSize:SIZEFONE];
            rightLabel.textAlignment = NSTextAlignmentCenter;
            rightLabel.text = @"元";
            rightLabel.textColor = UICOLOR(182, 0, 12, 1.0);

            [bgView addSubview:rightLabel];
            
            
            UILabel *countLabel = [[UILabel alloc]initWithFrame:CGRectMake(80, 5, APP_VIEW_WIDTH-80-30, 30)];
            countLabel.textAlignment = NSTextAlignmentRight;
            countLabel.backgroundColor = [UIColor clearColor];
            countLabel.font = [UIFont systemFontOfSize:SIZEFONE];
            countLabel.text = @"0";
            countLabel.textColor = UICOLOR(182, 0, 12, 1.0);
            countLabel.tag = 100;
            [bgView addSubview:countLabel];

        }
        
        UILabel *countLabel = (UILabel *)[cell viewWithTag:100];
        countLabel.text = self.UserReallyPay.length>0?self.UserReallyPay:@"0";
        
        return cell;
        
    }
    else if (indexPath.section ==7) {
        static NSString *identifier = @"payCell7";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;

            UIButton *nextButton = [[UIButton alloc]initWithFrame:CGRectMake(15, 15, APP_VIEW_WIDTH-30, 40)];
            nextButton.layer.cornerRadius = 5;
            nextButton.layer.masksToBounds = YES;
            [nextButton setTitle:@"下一步" forState:UIControlStateNormal];
            [nextButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
            nextButton.backgroundColor = UICOLOR(182, 0, 12, 1.0);
            [nextButton addTarget:self action:@selector(clickNext) forControlEvents:UIControlEventTouchUpInside];
            [cell addSubview:nextButton];
        }
        return cell;
        
    }

    static NSString *identifier = @"abcd";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.backgroundColor = [UIColor clearColor];
    }
    return cell;
    
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if (section ==0) {  //说明
        NSString *hasSendCoupon =[NSString stringWithFormat:@"%@",[self.resultDic objectForKey:@"hasSendCoupon"]];
        if ([hasSendCoupon intValue]==1) {
            return 1;
        }else{
            return 0;

        }
 
    }else if (section ==1){ //消费金额
        return 1;
    }else if (section ==2){  //不参加优惠金额
        return self.isNeedDiscount?2:1;
    }else if (section ==3){  //优惠券
        if(self.isCard){
            int i = 0;
            for (NSString *str in self.conpoutypeArray) {
                if ([str isEqualToString:@"1"]) {
                    i = i+1;
                }
            }
            return  i;
        }else{
            return 1;
        }
    }else if (section ==4){
          return 1;
    }else if (section ==5){  //首单
        NSString *isFirst =  [NSString stringWithFormat:@"%@",[self.resultDic objectForKey:@"isFirst"]]; ///
        if ([isFirst intValue]==1) {
            return 1;

        }else{
            return 0;
        }
     }
    return 1;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.section == 0) {
        NSString *sendCoupon =[NSString stringWithFormat:@"%@",[self.resultDic objectForKey:@"sendCoupon"]];
        CGSize size= [sendCoupon boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-80-10, MAXFLOAT)
                                              options:NSStringDrawingUsesLineFragmentOrigin
                                           attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:SIZEFONE]}
                                              context:nil].size;
        return size.height>30?size.height+10:50;
    }
    else  if (indexPath.section == 2) {
        if (indexPath.row==0) {
            return 50;

        }else{
            return self.B_isInputDisCount?65 : 25;
        }
    }
    else if(indexPath.section == 4){   //红包
        int row = 0;
        NSDictionary *bonus = [self.resultDic objectForKey:@"bonus"];
        NSString *canUsePlatBonus =[NSString stringWithFormat:@"%@",[bonus objectForKey:@"canUsePlatBonus"]];
        NSString *canUseShopBonus =[NSString stringWithFormat:@"%@",[bonus objectForKey:@"canUseShopBonus"]];
        
        if ([canUsePlatBonus intValue] ==1) {
            row = row+1;
        }
        if ([canUseShopBonus intValue] ==1) {
            row = row+1;
        }
        NSDictionary *card = [self.resultDic objectForKey:@"card"];
        NSString *canUseCard = [NSString stringWithFormat:@"%@",[card objectForKey:@"canUseCard"]];
        if ([canUseCard intValue] ==1) {
            row = row+1;
        }
        
        NSDictionary *icbc = [self.resultDic objectForKey:@"icbc"];
        NSString *canDiscount = [NSString stringWithFormat:@"%@",[icbc objectForKey:@"canDiscount"]];
        if ([canDiscount intValue] ==1) {
            row = row+1;
        }
        
        return 41*row;
    }
    else if (indexPath.section==5) {
        return 35;
    }
    else if (indexPath.section ==7){
        return 200;
    }
    else{
        return 50;
    }
}
-(CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    return 1;
}
-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 1;
}
-(UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    UIView *view = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 0, 1)];
    view.backgroundColor = [UIColor clearColor];
    return view;
}
-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    UIView *view = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 0, 1)];
    view.backgroundColor = [UIColor clearColor];
    return view;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.section == 2) {
        if (indexPath.row ==1) {
            self.B_isInputDisCount = !self.B_isInputDisCount;
            UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
            UIButton *button = (UIButton *)[cell viewWithTag:100];
            button.selected = self.B_isInputDisCount;
            if (!self.B_isInputDisCount) {
                self.B_inputDisCount = @"0";
            }
            
             NSIndexPath *indexPath=[NSIndexPath indexPathForRow:1 inSection:2];
            [tableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:indexPath,nil] withRowAnimation:UITableViewRowAnimationNone];
            [self CalculationResults];
        }
    }
   else if(indexPath.section ==3){
        
        if (self.isCard) {
            if ([self.consumAmount floatValue] > 0) {
                
                BMSQ_couponListViewController *vc = [[BMSQ_couponListViewController alloc]init];
                vc.shopCode = self.shopCode;
                vc.consumeAmount = self.consumAmount;
                
                NSString *discount = [self.conpoutypeArray objectAtIndex:0];
                NSString *reduction = [self.conpoutypeArray objectAtIndex:1];
                if ([discount isEqualToString:@"1"] && [reduction isEqualToString:@"1"]) {
                    
                    if (indexPath.row == 0) {
                        vc.couponType = @"3";
//                        if ([self.consumAmount floatValue] <self.minReduction) {
//                            [self alertViewMessage:[NSString stringWithFormat:@"最少消费%0.f元,才能有抵扣券使用",self.minReduction]];
//                            return;
//
//                        }
                        
                    }else{
                        vc.couponType = @"4";
//                        if ([self.consumAmount floatValue] <self.minDiscount) {
//                            [self alertViewMessage:[NSString stringWithFormat:@"最少消费%0.f元,才能有折扣券使用",self.minDiscount]];
//
//                            return;
//
//                        }
                    }
                    
                }else{
                    
                    if ([discount isEqualToString:@"1"]) {
                        vc.couponType = @"4";
//                        if ([self.consumAmount floatValue] <self.minDiscount) {
//                            [self alertViewMessage:[NSString stringWithFormat:@"最少消费%0.f元,才能有折扣券使用",self.minDiscount]];
//
//                            return;
//
//                        }
                    }else{
                        vc.couponType = @"3";
//                        if ([self.consumAmount floatValue] <self.minReduction) {
//                            [self alertViewMessage:[NSString stringWithFormat:@"最少消费%0.f元,才能有抵扣券使用",self.minReduction]];
//
//                            return;
//                        }
                        
                    }
                    
                }
                
                self.section = indexPath.section;
                self.row = indexPath.row;
                
                [self.navigationController pushViewController:vc animated:YES];
                
                
            }else{
                UIAlertView *alertView = [[UIAlertView alloc]initWithTitle:nil message:@"请输入金额" delegate:nil cancelButtonTitle:nil
                                                         otherButtonTitles:@"OK", nil];
                [alertView show];
                
                
                return;
            }
            
            
        }
   }
   else{
       NSIndexPath *index = [NSIndexPath indexPathForRow:0 inSection:2];
       UITableViewCell *cell = [tableView cellForRowAtIndexPath:index];
       UITextField *textField = (UITextField *)[cell viewWithTag:50000];
       [textField resignFirstResponder];
       
       index = [NSIndexPath indexPathForRow:1 inSection:2];
       cell = [tableView cellForRowAtIndexPath:index];
       textField = (UITextField *)[cell viewWithTag:80000];
       [textField resignFirstResponder];

       index = [NSIndexPath indexPathForRow:0 inSection:4];
       cell = [tableView cellForRowAtIndexPath:index];
       UIView *bgview =(UIView *)[cell viewWithTag:100];
       for (UIView *text in bgview.subviews) {
           for (id subView in text.subviews) {
               if ([subView isKindOfClass:[UITextField class]]) {
                   UITextField *textF = subView;
                   [textF resignFirstResponder];
               }
           }
       }
   }
}
-(void)alertViewMessage:(NSString *)str{
    UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"温馨提示" message:str delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
    [alterView show];
}


#pragma mark ----请求数据

-(void)listUserPayInfo{
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:self.shopCode forKey:@"shopCode"];
    [params setObject:self.batchCouponCode.length>0?self.batchCouponCode:@"" forKey:@"batchCouponCode"];
    [params setObject:self.consumAmount.length>0?self.consumAmount:@"" forKey:@"consumeAmount"];   //金额
    [SVProgressHUD showWithStatus:@""];
    
    NSString* vcode = [gloabFunction getSign:@"listUserPayInfo" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"listUserPayInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        wself.resultDic = responseObject;
        wself.haveData = YES;
        
        
        wself.minRealPay = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"minRealPay"]];
        NSDictionary *bonus = [wself.resultDic objectForKey:@"bonus"];
        NSString *canUsePlatBonus = [NSString stringWithFormat:@"%@",[bonus objectForKey:@"canUsePlatBonus"]];
        NSString *canUseShopBonus =[NSString stringWithFormat:@"%@",[bonus objectForKey:@"canUseShopBonus"]];
        
        if ([canUsePlatBonus intValue] ==1) {
            [wself.bounsArray replaceObjectAtIndex:0 withObject:@"1"];
        }
        if ([canUseShopBonus intValue] ==1) {
            [wself.bounsArray replaceObjectAtIndex:1 withObject:@"1"];
        }
        NSDictionary *card = [self.resultDic objectForKey:@"card"];
        NSString *canUseCard = [NSString stringWithFormat:@"%@",[card objectForKey:@"canUseCard"]];
        if ([canUseCard intValue] ==1) {
            [wself.bounsArray replaceObjectAtIndex:2 withObject:@"1"];
            wself.memberCardDisCount = [NSString stringWithFormat:@"%@",[card objectForKey:@"discount"]];
        }
        
        NSDictionary *icbc = [self.resultDic objectForKey:@"icbc"];
        NSString *canDiscount = [NSString stringWithFormat:@"%@",[icbc objectForKey:@"canDiscount"]];
        if ([canDiscount intValue] ==1) {
            [wself.bounsArray replaceObjectAtIndex:3 withObject:@"1"];
            wself.cardDiscount = [NSString stringWithFormat:@"%@",[icbc objectForKey:@"onlinePaymentDiscount"]];
        }
        
        if ([responseObject objectForKey:@"coupon"]) {
            NSDictionary *coupon =  [responseObject objectForKey:@"coupon"];
            if (wself.isCard) {
                NSString *discount = [NSString stringWithFormat:@"%@",[coupon objectForKey:@"discount"]];
                NSString *reduction = [NSString stringWithFormat:@"%@",[coupon objectForKey:@"reduction"]];

                [wself.conpoutypeArray addObject:discount];
                [wself.conpoutypeArray addObject:reduction];
            }
            
            if ([[coupon objectForKey:@"couponInfo"]isKindOfClass:[NSDictionary class]]) {
                NSDictionary *couponInfo = [coupon objectForKey:@"couponInfo"];
                wself.conpoutype = [NSString stringWithFormat:@"%@",[couponInfo objectForKey:@"couponType"]];
                if ([wself.conpoutype isEqualToString:@"3"]) {
                    wself.count = [[NSString stringWithFormat:@"%@",[couponInfo objectForKey:@"availablePrice"]] floatValue];
                    wself.dis = [[NSString stringWithFormat:@"%@",[couponInfo objectForKey:@"insteadPrice"]] floatValue];
                    wself.limitedNbr = [[NSString stringWithFormat:@"%@",[couponInfo objectForKey:@"limitedNbr"]]intValue];
                    wself.userCount = [[NSString stringWithFormat:@"%@",[couponInfo objectForKey:@"userCount"]]intValue];
                }
            }
            
            if ([coupon objectForKey:@"minDiscount"]) {
                wself.minDiscount = [[NSString stringWithFormat:@"%@",[coupon objectForKey:@"minDiscount"]] floatValue];
                
            }
            if ([coupon objectForKey:@"minReduction"]) {
                wself.minReduction = [[NSString stringWithFormat:@"%@",[coupon objectForKey:@"minReduction"]] floatValue];
            }
        }
        
        NSString *isFirst = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"isFirst"]];
        if (![isFirst isEqualToString:@"0"]) {
            wself.mealFirstDec = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"mealFirstDec"]];
        }else{
            wself.mealFirstDec = @"0";

        }
        
        [wself.my_tableView reloadData];
        
        [wself CalculationResults];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
    
}

#pragma mark -- 监听textField --
-(void)chnageTextFild:(UITextField *)textfield{
    
    if (textfield.tag == 50000) {   //花费金额
        if (textfield.text.length>6) {
            textfield.text = [textfield.text substringToIndex:6];
        }
        self.consumAmount = textfield.text;
        
        self.useLimitedNbr = (int)[self.consumAmount floatValue]/self.count;
        self.useLimitedNbr = self.useLimitedNbr>=self.userCount?self.userCount:self.useLimitedNbr;
        
    }else if (textfield.tag ==60000){ //惠圈红包
        NSDictionary *bonus = [self.resultDic objectForKey:@"bonus"];
        NSString *platB =[NSString stringWithFormat:@"%@",[bonus objectForKey:@"platBonus"]];
        float platBF = [platB floatValue];
        float textF = [textfield.text floatValue];
        if (textF<=platBF) {
            self.plantBouns = textfield.text;
        }else{
            textfield.text = platB;
            self.plantBouns = platB;
        }
        
        
        if ([self.consumAmount floatValue]<=[self.B_inputDisCount floatValue]) {
            UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"不能享受此优惠" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
            [alterView show];
            
            self.plantBouns =@"0";
            textfield.text = @"0";

        }else{
            float i =[self.consumAmount floatValue]-[self.B_inputDisCount floatValue]-[self.shopBouns floatValue];
            if ([self.plantBouns floatValue]>i) {
                
                NSString *result = [NSString stringWithFormat:@"此消费最多使用优惠%0.2f元",i];
                UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:result delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
                [alterView show];
                
                self.plantBouns =[NSString stringWithFormat:@"%0.2f",i];
                textfield.text = [NSString stringWithFormat:@"%0.2f",i];
            }
        
        }
        
    }else if (textfield.tag ==70000){   //商家红包
        NSDictionary *bonus = [self.resultDic objectForKey:@"bonus"];
        NSString *shopB = [NSString stringWithFormat:@"%@",[bonus objectForKey:@"shopBonus"]];
        float shopBF = [shopB floatValue];
        float textF = [textfield.text floatValue];
        if (textF<=shopBF) {
            self.shopBouns = textfield.text;
        }else{
            textfield.text = shopB;
            self.shopBouns = shopB;
        }
        
        if ([self.consumAmount floatValue]<=[self.B_inputDisCount floatValue]) {
            UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"不能享受此优惠" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
            [alterView show];
            self.shopBouns =@"0";
            textfield.text = @"0";
        }else{
            
            float i =[self.consumAmount floatValue]-[self.B_inputDisCount floatValue]-[self.plantBouns floatValue];
            if ([self.shopBouns floatValue]>i) {
                NSString *result = [NSString stringWithFormat:@"此消费最多使用优惠%0.2f元",i];
                UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:result delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
                [alterView show];
                self.shopBouns =[NSString stringWithFormat:@"%0.2f",i];
                textfield.text = [NSString stringWithFormat:@"%0.2f",i];
            }

        }
        
    }else if(textfield.tag ==80000){  //不参加优惠金额
        if (textfield.text.length>6) {
            textfield.text = [textfield.text substringToIndex:6];
        }
        self.B_inputDisCount = textfield.text;
        if ([self.B_inputDisCount floatValue]>[self.consumAmount floatValue]) {
         
            
        }

    }
    
    NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:3];
    [self.my_tableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationNone];
    [self CalculationResults];

}


-(void)addNbr:(UIButton *)button{
    
    if (button.tag == 77) {
        //减
        self.nbr = self.nbr-1;
        if (self.nbr <1) {
            self.nbr = 0;
        }
    }else{
        //加
        
        if([self.seleCoupon objectForKey:@"availablePrice"]){
            self.nbr = 1;
            
        }else{
            int use = 0;
            if(self.limitedNbr ==0)
            {
                use =self.useLimitedNbr;
            }
            else
            {
                use = self.useLimitedNbr>self.limitedNbr?self.limitedNbr:self.useLimitedNbr;
            }
            self.nbr = self.nbr+1;
            
            if (self.nbr >= use) {
                self.nbr = use;
            }
        }
    }
    NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:3];
    [self.my_tableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationNone];
    
    [self CalculationResults];
}
#pragma mark -- 计算结果 --
-(void)CalculationResults{
    if ([self.consumAmount floatValue]>0) {
        
        NSDictionary *coupon = [self.resultDic objectForKey:@"coupon"];
        NSDictionary *couponInfo = [coupon objectForKey:@"couponInfo"];
        
        if ([[couponInfo objectForKey:@"couponType"]integerValue]==4) {
            NSString *availablePrice =  [NSString stringWithFormat:@"%@",[couponInfo objectForKey:@"availablePrice"]];
            if ([self.consumAmount floatValue]>= [availablePrice floatValue]) {
                NSString *discountPercent = [NSString stringWithFormat:@"%@",[couponInfo objectForKey:@"discountPercent"]];
                float result = self.consumAmount.floatValue -(self.consumAmount.floatValue*[discountPercent floatValue]/10);
                self.disMoney = [NSString stringWithFormat:@"%0.2f",result];
            }else{
                self.disMoney=@"0";
            }
            
          
        }  //优惠金额
        else{
            if ([[self.seleCoupon objectForKey:@"couponType"]integerValue]==4) {
                NSString *availablePrice =  [NSString stringWithFormat:@"%@",[self.seleCoupon objectForKey:@"availablePrice"]];
                if ([self.consumAmount floatValue]>= [availablePrice floatValue]) {
                    NSString *discountPercent = [NSString stringWithFormat:@"%@",[self.seleCoupon objectForKey:@"discountPercent"]];
                    float result = self.consumAmount.floatValue -(self.consumAmount.floatValue*[discountPercent floatValue]/10);
                    self.disMoney = [NSString stringWithFormat:@"%0.2f",result];
                }else{
                    self.disMoney=@"0";
                    NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:self.section];
                    [self.my_tableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];
                
                
                }
            }else if ([[self.seleCoupon objectForKey:@"couponType"]integerValue]==3){
//                NSString *availablePrice =  [NSString stringWithFormat:@"%@",[self.seleCoupon objectForKey:@"availablePrice"]];
//                if ([self.consumAmount floatValue]>= [availablePrice floatValue]) {
//                    NSString *discountPercent = [NSString stringWithFormat:@"%@",[self.seleCoupon objectForKey:@"discountPercent"]];
//                    float result = self.consumAmount.floatValue -(self.consumAmount.floatValue*[discountPercent floatValue]/10);
//                    self.disMoney = [NSString stringWithFormat:@"%0.2f",result];
//                }else{
//                    self.disMoney=@"0";
//                    NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:self.section];
//                    [self.my_tableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];
//                    
//                    
//                }

            }
            
        }
        
        self.cardDiscount = [self.cardDiscount floatValue]>0?self.cardDiscount:@"10";  //工行卡折扣
        self.memberCardDisCount = [self.memberCardDisCount floatValue]>0?self.memberCardDisCount:@"10"; //会员卡折扣
        float bouns =[self.shopBouns floatValue]+[self.plantBouns floatValue]; //红包
        float mealFirs = [self.mealFirstDec floatValue]>0?[self.mealFirstDec floatValue]:0.0;  //首单立减
        float noDiscountPrice = [self.B_inputDisCount floatValue];
        
        //(((((全部金额-不打折金额)-优惠券折扣)*会员卡折扣）-红包）*工行卡折扣）- 首单立减
       float resultS = (((([self.consumAmount floatValue] - noDiscountPrice))- [self.disMoney floatValue])*([self.memberCardDisCount floatValue]/10) -bouns) * [self.cardDiscount floatValue]/10 - mealFirs;
        
        if (resultS<=0) {
            resultS = 0;
        }
        float pay = resultS + noDiscountPrice;
        if (pay<=0) {
            self.UserReallyPay = @"0.00";
        }else{
            self.UserReallyPay = [NSString stringWithFormat:@"%0.2f",pay];
        }
        
        
    }else{
        self.UserReallyPay = @"0";
        
    }
    
    NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:6];
    [self.my_tableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationNone];

}

#pragma mark  下一步
-(void)clickRight:(UIButton *)btn{
    [self clickNext];
}
-(void)clickNext{
    
     if ([self.consumAmount floatValue]<=0) {
        
        UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"请输入金额" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
        [alterView show];
        return;
    }
   else if([self.UserReallyPay floatValue]<1 && [self.mealFirstDec floatValue]>0){
        
        UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"温馨提示" message:@"您在享受首单立减的优惠条件下，支付金额小于1,我们将默认您要支付1元!" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
        alterView.tag = 100;
        [alterView show];
       return;

    }
   else if([self.B_inputDisCount floatValue]> [self.consumAmount floatValue]){
       UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"不参加优惠金额不能多于总金额" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
       [alterView show];
       return;


   } else if( [self.consumAmount floatValue]-[self.B_inputDisCount floatValue]<[self.shopBouns floatValue]+[self.plantBouns floatValue]){
       NSString *result = [NSString stringWithFormat:@"共可优惠%0.2f元",[self.consumAmount floatValue]-[self.B_inputDisCount floatValue]];
       UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:result delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
       [alterView show];
       return;

   }else if( [self.minRealPay floatValue]>[self.UserReallyPay floatValue]){
       NSString *result = [NSString stringWithFormat:@"最低消费%@元",self.minRealPay];
       UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:result delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
       [alterView show];
       return;
       
   }
   else{
       [self gotoPay];
 
   }
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    
    if(buttonIndex == 1){
        [self gotoPay];
    }
}
-(void)gotoPay{
    
    BMSQ_PayCardViewController *vc = [[BMSQ_PayCardViewController alloc]init];
    vc.fromVC = self.fromVC;
    vc.shopCode = self.shopCode;
    vc.consumeAmount = self.consumAmount;  //花费钱数
    vc.batchCouponCode = self.batchCouponCode;
    if (self.type == 4) {
        vc.nbrCoupon =  @"1";  //优惠张数
    }else{
        vc.nbrCoupon =  [NSString stringWithFormat:@"%d",self.nbr];  //优惠张数
        
    }
    
    vc.platBonus = [self.plantBouns floatValue]>0?self.plantBouns:@"0";
    vc.shopBonus = [self.shopBouns floatValue]>0?self.shopBouns:@"0";
    vc.payType = @"1";   //支付方式
    vc.shopName = self.shopName;
    vc.isTakeout = self.isTakeOut;
    vc.isdining = self.isDinning;
    vc.orderCode = self.orderCode;
    vc.orderNbr = self.orderNbr;
    vc.noDiscountPrice = self.B_inputDisCount.length>0?self.B_inputDisCount:@"0";
    [self.navigationController pushViewController:vc animated:YES];

}
-(void)refreshSeleCoupon:(NSNotification *)notification{
     self.seleCoupon =  notification.object ;
    if ([[NSString stringWithFormat:@"%@",[self.seleCoupon objectForKey:@"couponType"]]isEqualToString:@"3"]) {
        self.nbr = [[NSString stringWithFormat:@"%@",[self.seleCoupon objectForKey:@"nbr"]]intValue] ;
        self.dis = [[NSString stringWithFormat:@"%@",[self.seleCoupon objectForKey:@"insteadPrice"]] floatValue];
        
        self.useLimitedNbr = (int)[self.consumAmount floatValue]/[[NSString stringWithFormat:@"%@",[self.seleCoupon objectForKey:@"availablePrice"]] floatValue];
        self.limitedNbr = [[NSString stringWithFormat:@"%@",[self.seleCoupon objectForKey:@"limitedNbr"]]intValue];
        self.userCount = [[NSString stringWithFormat:@"%@",[self.seleCoupon objectForKey:@"userCount"]]intValue];
        
        self.useLimitedNbr = self.useLimitedNbr>=self.userCount?self.userCount:self.useLimitedNbr;

    }else{
        self.nbr = 1;

    }
    self.batchCouponCode = [self.seleCoupon objectForKey:@"batchCouponCode"];
    NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:self.section];
    [self.my_tableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];
    [self CalculationResults];
}

-(BOOL)textFieldShouldBeginEditing:(UITextField *)textField{
    
    if (textField.tag ==60000 ||textField.tag ==70000) {
        [UIView animateWithDuration:0.2 animations:^{
            self.my_tableView.frame = CGRectMake(0, -60, self.my_tableView.frame.size.width, self.my_tableView.frame.size.height);
        }];
    }else if (textField.tag==50000){
        if ([textField.text isEqualToString:@"0"]) {
            textField.text = @"";
        }
    }
    return YES;
}

-(BOOL)textFieldShouldReturn:(UITextField *)textField{
    
    [textField resignFirstResponder];
    
    [UIView animateWithDuration:0.2 animations:^{
        self.my_tableView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT);
    }];
    return YES;
}

-(void)keyboardHiddend{
    [UIView animateWithDuration:0.2 animations:^{
        self.my_tableView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT);
    }];
}


@end
