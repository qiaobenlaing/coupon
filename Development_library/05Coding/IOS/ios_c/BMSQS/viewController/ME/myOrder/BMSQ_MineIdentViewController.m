//
//  BMSQ_MineIdentViewController.m
//  BMSQC
//
//  Created by  on 15/9/6.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_MineIdentViewController.h"
#import "BMSQ_MineIdentCell.h"

#import "BMSQ_OrderDetailController.h"
#import "SVProgressHUD.h"

#import "MJRefresh.h"

@interface BMSQ_MineIdentViewController ()<UITableViewDataSource,UITableViewDelegate>

@property (nonatomic, strong)UITableView *m_tableView;
@property (nonatomic, strong)UIImageView *iv_noData;
@property (nonatomic, strong)NSMutableArray *finshArray;
@property (nonatomic, strong)NSMutableArray *n_finshArray;
@property (nonatomic, strong)NSString *currentStatus; //1完成 0 未完成

@property (nonatomic, assign)int finishPage;
@property (nonatomic, assign)int N_finishPage;

@end

@implementation BMSQ_MineIdentViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.finshArray = [[NSMutableArray alloc]init];
    self.n_finshArray = [[NSMutableArray alloc]init];
    self.currentStatus = @"1";
    self.finishPage = 1;
    self.N_finishPage = 1;
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(reloadNorderList) name:@"freshNorderList" object:nil];
    [self setViewUp];
}

-(void)reloadNorderList{
    
    [self.n_finshArray removeAllObjects];
    self.N_finishPage = 1;
    self.currentStatus = @"0";
    [self getUserOrderList];

    
}

- (void)setViewUp{
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"我的订单"];
    
    
    NSArray *segmentedArray=[[NSArray alloc]initWithObjects:@"未完成",@"已完成",nil];
   UISegmentedControl *segmentedControl=[[UISegmentedControl alloc]initWithItems:segmentedArray];
    segmentedControl.frame=CGRectMake(APP_VIEW_WIDTH/2-70 ,APP_VIEW_ORIGIN_Y+10 ,140,30);
    segmentedControl.selectedSegmentIndex=0;//默认选中项索引（计数是从0开始的)
    segmentedControl.tintColor=APP_NAVCOLOR;//设置背景颜色
    [segmentedControl setTitle:@"已完成"forSegmentAtIndex:0];//设置指定索引的标题
    [segmentedControl setTitle:@"未完成"forSegmentAtIndex:1];//设置指定索引的标题
    [segmentedControl addTarget:self action:@selector(segmentedAction:) forControlEvents:UIControlEventValueChanged];

    [self.view addSubview:segmentedControl];
    
    
    self.m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, segmentedControl.frame.size.height + APP_VIEW_ORIGIN_Y+20, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - APP_VIEW_ORIGIN_Y-20-segmentedControl.frame.size.height)];
    self.m_tableView.dataSource = self;
    self.m_tableView.delegate = self;
    self.m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.m_tableView.backgroundColor = [UIColor clearColor];
    
    [self.m_tableView addFooterWithTarget:self action:@selector(freshFootTableView)];
    
    [self.view addSubview:self.m_tableView];
    
    self.iv_noData = [[UIImageView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y , APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
    self.iv_noData.hidden = YES;
    self.iv_noData.image = [UIImage imageNamed:@"iv_noMessage"];
    self.iv_noData.contentMode =  UIViewContentModeScaleAspectFill;
    [self.view addSubview:self.iv_noData];
    [self getUserOrderList];
    
}
-(void)segmentedAction:(UISegmentedControl *)Seg
{
    NSInteger index=Seg.selectedSegmentIndex;
    switch (index) {
        case 0:
        {
            self.currentStatus = @"1";
            
            if (self.finshArray.count>0) {
                self.iv_noData.hidden = YES;
                [self.m_tableView reloadData];
 
            }else{
                [self getUserOrderList];
 
            }
            
 
        }
            
            break;
        case 1:
        {
            self.currentStatus = @"0";
            if (self.n_finshArray.count>0) {
                self.iv_noData.hidden = NO;

                [self.m_tableView reloadData];
            }else{
                [self getUserOrderList];
            }

        }
            break;
        default:
            break;
    }
}
- (void)getUserOrderList
{
    [self initJsonPrcClient:@"2"];
    [SVProgressHUD showWithStatus:@""];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    if ([self.currentStatus isEqualToString:@"1"]) {
        [params setObject:[NSString stringWithFormat:@"%d",self.finishPage] forKey:@"page"];
    }else{
        [params setObject:[NSString stringWithFormat:@"%d",self.N_finishPage] forKey:@"page"];
    }
    [params setObject:self.currentStatus forKey:@"isFinish"];
    NSString* vcode = [gloabFunction getSign:@"getUserOrderList" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"getUserOrderList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        NSArray *orderList =[responseObject objectForKey:@"orderList"];
        
        if ([weakSelf.currentStatus isEqualToString:@"1"]) {
            [weakSelf.finshArray addObjectsFromArray:orderList];
            if(weakSelf.finshArray.count>0 ){
                weakSelf.iv_noData.hidden = YES;
            }else{
                weakSelf.iv_noData.hidden = NO;

            }
            
            
        }else{
            [weakSelf.n_finshArray addObjectsFromArray:orderList];
            
            if(weakSelf.n_finshArray.count>0 ){
                weakSelf.iv_noData.hidden = YES;
            }else{
                weakSelf.iv_noData.hidden = NO;
                
            }
            

        }
        
        if (orderList.count>0) {
            if ([weakSelf.currentStatus isEqualToString:@"1"]) {
                weakSelf.finishPage = weakSelf.finishPage+1;
            }else{
                weakSelf.N_finishPage = weakSelf.N_finishPage+1;
            }
        }
        
    
        
        
        
        
        
        [weakSelf.m_tableView reloadData];
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];

        
    }];
}

//列表数据
/*
- (void)getOrderDetails
{
    
    
    
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
//    [params setObject:self.consumeCode forKey:@"consumeCode"];
//    NSString* vcode = [gloabFunction getSign:@"getConsumeInfo" strParams:self.consumeCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
//    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"getConsumeInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
        
        NSString *orderNbr = [responseObject objectForKey:@""];
        
//        m_dic = responseObject;
        
        [m_tableView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
    }];
    
}

*/




#pragma mark - UITableView delegate
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if ([self.currentStatus isEqualToString:@"1"]) {
        return self.finshArray.count;
    }else{
        return self.n_finshArray.count;
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{

    static NSString *cellIdentifier = @"Cell";
    BMSQ_MineIdentCell *cell = (BMSQ_MineIdentCell *)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
    if (cell == nil)
    {
        cell = [[BMSQ_MineIdentCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
        cell.backgroundColor = APP_VIEW_BACKCOLOR;
        cell.selectionStyle=UITableViewCellSelectionStyleNone;
        
    }
    
    if ([self.currentStatus isEqualToString:@"1"]) {
        [cell setCellValue:[self.finshArray objectAtIndex:indexPath.row] status:@"已完成"];
 
    }else{
        [cell setCellValue:[self.n_finshArray objectAtIndex:indexPath.row] status:@"未完成"];
    }
    
 

    return cell;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 140;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSDictionary *dicData;
    BMSQ_OrderDetailController* detailCtrl = [[BMSQ_OrderDetailController alloc]init];
    if ([self.currentStatus isEqualToString:@"1"]) {
        dicData = [self.finshArray objectAtIndex:indexPath.row];
        detailCtrl.isFinsh = YES;
    }else{
        dicData = [self.n_finshArray objectAtIndex:indexPath.row];
        detailCtrl.isFinsh = NO;
    }
    detailCtrl.consumeCode = [dicData objectForKey:@"consumeCode"];
    detailCtrl.couponDic = dicData;
    [self.navigationController pushViewController:detailCtrl animated:YES];
    
}
-(void)freshFootTableView{
    
    [self getUserOrderList];
    [self.m_tableView footerEndRefreshing];
}

@end
