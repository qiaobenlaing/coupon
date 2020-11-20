//
//  BMSQ_MyNoticeController.m
//  BMSQC
//
//  Created by djx on 15/8/9.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_MyNoticeController.h"
#import "Coupon_ReceiveTableViewCell.h"


#import "MJRefresh.h"
#import "SVProgressHUD.h"

#import "BMSQ_ChatViewController.h"


@interface BMSQ_MyNoticeController ()
{
    UITableView * m_tableView;
    NSMutableArray* m_dataSource;
    BOOL refreshing;
    int status; //1 优惠券 2 惠员卡 3 会员沟通 4 异业广播
    UIButton* btnCupon;
    UIButton* btnCard;
    UIButton* btnShop;
    UIButton* btnGB; //广播
}



@property (nonatomic, strong)UITableView *m_tableView;
@property (nonatomic,strong)NSMutableArray *couponArray; //优惠券
@property (nonatomic,strong)NSMutableArray *cardArray;   //惠员卡
@property (nonatomic,strong)NSMutableArray *shopArray;   //异业广播
@property (nonatomic,assign)int pageNumber;

@property (nonatomic,strong)NSMutableArray *ChatArray;   //会员沟通
@property (nonatomic,assign)int chatPageNumber;

@property (nonatomic, strong)UIImageView *m_noDataView;




@end

@implementation BMSQ_MyNoticeController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.couponArray = [[NSMutableArray alloc]init];
    self.cardArray = [[NSMutableArray alloc]init];
    self.shopArray = [[NSMutableArray alloc]init];
    self.ChatArray = [[NSMutableArray alloc]init];

    [self setViewUp];
}



- (void)setViewUp
{
    [self setNavigationBar];
    [self setNavTitle:@"我的消息"];
    [self setNavBackItem];
    
    self.pageNumber = 1;
    self.chatPageNumber = 1;
    UIView* v_historyTop = [[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 50)];
    v_historyTop.backgroundColor = [UIColor whiteColor];
    
    
    btnCupon = [[UIButton alloc]initWithFrame:CGRectMake(20, 10, (APP_VIEW_WIDTH-40)/4, 30)];
    [btnCupon setTitle:@"优惠券" forState:UIControlStateNormal];
    [btnCupon.titleLabel setFont:[UIFont systemFontOfSize:14]];
    [btnCupon setBackgroundImage:[UIImage imageNamed:@"iv_leftNoSelect"] forState:UIControlStateNormal];
    [btnCupon setBackgroundImage:[UIImage imageNamed:@"iv_leftSelect"] forState:UIControlStateSelected];
    [btnCupon setTitleColor:UICOLOR(177, 0, 0, 1.0) forState:UIControlStateNormal];
    [btnCupon setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
    [btnCupon addTarget:self action:@selector(btnCuponClick) forControlEvents:UIControlEventTouchUpInside];
    btnCupon.selected = YES;
    [v_historyTop addSubview:btnCupon];
    
    btnCard = [[UIButton alloc]initWithFrame:CGRectMake(20+(APP_VIEW_WIDTH-40)/4, 10, (APP_VIEW_WIDTH-40)/4, 30)];
    [btnCard setTitle:@"惠员卡" forState:UIControlStateNormal];
    [btnCard.titleLabel setFont:[UIFont systemFontOfSize:14]];
    [btnCard setBackgroundImage:[UIImage imageNamed:@"iv_centerNoselect"] forState:UIControlStateNormal];
    [btnCard setBackgroundImage:[UIImage imageNamed:@"iv_centerSelect"] forState:UIControlStateSelected];
    [btnCard addTarget:self action:@selector(btnCardClick) forControlEvents:UIControlEventTouchUpInside];
    [btnCard setTitleColor:UICOLOR(177, 0, 0, 1.0) forState:UIControlStateNormal];
    [btnCard setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
    [v_historyTop addSubview:btnCard];
    
    btnShop = [[UIButton alloc]initWithFrame:CGRectMake(20+2*(APP_VIEW_WIDTH-40)/4, 10, (APP_VIEW_WIDTH-40)/4, 30)];
    [btnShop setTitle:@"会员沟通" forState:UIControlStateNormal];
    [btnShop.titleLabel setFont:[UIFont systemFontOfSize:14]];
    [btnShop setBackgroundImage:[UIImage imageNamed:@"iv_centerNoselect"] forState:UIControlStateNormal];
    [btnShop setBackgroundImage:[UIImage imageNamed:@"iv_centerSelect"] forState:UIControlStateSelected];
    [btnShop addTarget:self action:@selector(btnShopClick) forControlEvents:UIControlEventTouchUpInside];
    [btnShop setTitleColor:UICOLOR(177, 0, 0, 1.0) forState:UIControlStateNormal];
    [btnShop setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
    [v_historyTop addSubview:btnShop];
    
    btnGB = [[UIButton alloc]initWithFrame:CGRectMake(20+3*(APP_VIEW_WIDTH-40)/4, 10, (APP_VIEW_WIDTH-40)/4, 30)];
    [btnGB setTitle:@"异业广播" forState:UIControlStateNormal];
    [btnGB.titleLabel setFont:[UIFont systemFontOfSize:14]];
    [btnGB setBackgroundImage:[UIImage imageNamed:@"iv_rightNoSelect"] forState:UIControlStateNormal];
    [btnGB setBackgroundImage:[UIImage imageNamed:@"iv_rightSelect"] forState:UIControlStateSelected];
    [btnGB addTarget:self action:@selector(btnGBClick) forControlEvents:UIControlEventTouchUpInside];
    [btnGB setTitleColor:UICOLOR(177, 0, 0, 1.0) forState:UIControlStateNormal];
    [btnGB setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
    [v_historyTop addSubview:btnGB];
    [self.view addSubview:v_historyTop];
    
    CGFloat hieght = 0;
    if (self.hidesBottomBarWhenPushed) {
        hieght = 44;
    }
    self.m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 50, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y-50 )];
    self.m_tableView.dataSource = self;
    self.m_tableView.delegate = self;
    self.m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.m_tableView.backgroundColor = [UIColor clearColor];
    [self.m_tableView addFooterWithTarget:self action:@selector(refreshFoot)];
    [self.view addSubview:self.m_tableView];
    status = 1;
    
    
    self.m_noDataView = [[UIImageView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 50, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y-50 )];
    self.m_noDataView.hidden = YES;
    self.m_noDataView.backgroundColor = [UIColor clearColor];;
    self.m_noDataView.image = [UIImage imageNamed:@"iv_noMessage"];
    
    [self.view addSubview:self.m_noDataView];
    [self.view bringSubviewToFront:self.m_noDataView];
    self.m_noDataView.hidden = YES;
    
    [self getMyNotice];

}
-(void)refreshFoot{
    if (status == 3) {

        [self getMsgGroup];
        
    }else{

        [self getMyNotice];
 
    }
    [self.m_tableView footerEndRefreshing];
}

- (void)btnCuponClick
{
    btnCupon.selected = YES;
    btnCard.selected = NO;
    btnShop.selected = NO;
    btnGB.selected = NO;
    status = 1;
    [self.m_tableView reloadData];
}

- (void)btnCardClick
{
    btnCupon.selected = NO;
    btnCard.selected = YES;
    btnShop.selected = NO;
    btnGB.selected = NO;
    status = 2;
    [self.m_tableView reloadData];

    

}

- (void)btnShopClick
{
    btnCupon.selected = NO;
    btnCard.selected = NO;
    btnShop.selected = YES;
    btnGB.selected = NO;
    status = 3;
    if (self.ChatArray.count ==0) {
        [self getMsgGroup];

    }
    [self.m_tableView reloadData];

}

- (void)btnGBClick
{
    btnCupon.selected = NO;
    btnCard.selected = NO;
    btnShop.selected = NO;
    btnGB.selected = YES;
    status = 4;
    [self.m_tableView reloadData];

}



#pragma mark tableview dataSource and delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(status == 1){
        if (self.couponArray.count ==0) {
            self.m_noDataView.hidden = NO;
        }else{
            self.m_noDataView.hidden = YES;
            
        }
        return self.couponArray.count;
        
    }else if (status ==2){
        
        if (self.cardArray.count ==0) {
            self.m_noDataView.hidden = NO;
        }else{
            self.m_noDataView.hidden = YES;
        }
        return self.cardArray.count;
       
    }else if (status ==3){
        
        if (self.ChatArray.count ==0) {
            self.m_noDataView.hidden = NO;
        }else{
            self.m_noDataView.hidden = YES;
        }

        return self.ChatArray.count;
        
    }else if (status ==4){
        
        if (self.shopArray.count ==0) {
            self.m_noDataView.hidden = NO;
        }else{
            self.m_noDataView.hidden = YES;
        }

        
        return self.shopArray.count;
    }
    
      return m_dataSource.count;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
        return 100;

}



- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
        //操作区域
    static NSString *identifier = @"Coupon_ReceiveTableViewCell";
    Coupon_ReceiveTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell ==nil) {
        cell = [[Coupon_ReceiveTableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    
    if(status == 1){
        [cell setReceiveCell:[self.couponArray objectAtIndex:indexPath.row]type:Coupon_FindCell];

    }else if (status ==2){
        [cell setReceiveCell:[self.cardArray objectAtIndex:indexPath.row]type:Coupon_CareCell];

    }else if (status ==3){
        //
        [cell setReceiveCell:[self.ChatArray objectAtIndex:indexPath.row]type:Coupon_chatCell];

    }else if (status ==4){
        [cell setReceiveCell:[self.shopArray objectAtIndex:indexPath.row]type:Coupon_shopCell];

    }
    return cell;

    
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (status == 3) {
        BMSQ_ChatViewController *vc = [[BMSQ_ChatViewController alloc]init];
        NSDictionary *dic = [self.ChatArray objectAtIndex:indexPath.row];
        if ([dic objectForKey:@"userName"]) {
            vc.titleSr =[dic objectForKey:@"userName"];
        }else{
            vc.titleSr =@"我的消息";

        }
        vc.userCode = [dic objectForKey:@"userCode"];
        
        [self.navigationController pushViewController:vc animated:YES];
    }
    
}

- (void)getMyNotice
{
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[NSNumber numberWithInt:self.pageNumber] forKey:@"page"];
    NSString* vcode = [gloabFunction getSign:@"listMsg" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    [SVProgressHUD showWithStatus:@""];
    __weak typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"listMsg" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        if([responseObject objectForKey:@"couponMsg"]){
            id dic = [responseObject objectForKey:@"couponMsg"];
            [weakSelf.couponArray addObjectsFromArray:[dic objectForKey:@"couponMsgList"]];
            
        }
        if ([responseObject objectForKey:@"cardMsg"]) {
            id dic = [responseObject objectForKey:@"cardMsg"];
            [weakSelf.cardArray addObjectsFromArray:[dic objectForKey:@"cardMsgList"]];
        }
        
        if ([responseObject objectForKey:@"shopMsg"]) {
            id dic = [responseObject objectForKey:@"shopMsg"];
            [weakSelf.shopArray addObjectsFromArray:[dic objectForKey:@"shopMsgList"]];
        }
        
        if(status == 1){
            if (weakSelf.couponArray.count ==0) {
                weakSelf.m_noDataView.hidden = NO;
            }else{
                weakSelf.m_noDataView.hidden = YES;

            }
            
            
        }else if (status ==2){
            
            if (weakSelf.cardArray.count ==0) {
                weakSelf.m_noDataView.hidden = NO;
            }else{
                weakSelf.m_noDataView.hidden = YES;
            }
            
        }else if (status ==4){
            
            if (weakSelf.shopArray.count ==0) {
                weakSelf.m_noDataView.hidden = NO;
            }else{
                weakSelf.m_noDataView.hidden = YES;
            }
            
            
        }

        
        
        weakSelf.pageNumber = weakSelf.pageNumber +1;
        [weakSelf.m_tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
    }];
}

- (void)getMsgGroup
{
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[NSNumber numberWithInt:self.chatPageNumber] forKey:@"page"];
    NSString* vcode = [gloabFunction getSign:@"getMsgGroup" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    [SVProgressHUD showWithStatus:@""];
    __weak typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"getMsgGroup" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        if ([responseObject objectForKey:@"ret"]) {
            [weakSelf.ChatArray addObjectsFromArray:[responseObject objectForKey:@"ret"]];
        }
        weakSelf.chatPageNumber = self.chatPageNumber +1;

        if (status ==3){
            
            if (weakSelf.ChatArray.count ==0) {
                weakSelf.m_noDataView.hidden = NO;
            }else{
                weakSelf.m_noDataView.hidden = YES;
            }
        }

        
        [weakSelf.m_tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
    }];
}

@end
